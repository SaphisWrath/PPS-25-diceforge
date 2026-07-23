package controller

import controller.ViewState.{MainMenu, MatchInit}
import javafx.event.ActionEvent
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.beans.property.ObjectProperty
import scalafx.stage.Stage
import view.MainStage
import view.scenes.{MainMenuScene, MatchInitScene}

enum ViewState:
  case MainMenu
  case MatchInit

trait ControllerStage:
  def init(): PrimaryStage
  def changeScene(newState: ViewState): Unit
  def getViewState: ViewState

object ControllerStage:
  private class ControllerStageImpl extends ControllerStage:
    private var viewState: ViewState = MainMenu
    private val mainStage: PrimaryStage = MainStage(this).stage
    changeScene(viewState)

    override def init(): PrimaryStage = mainStage

    override def changeScene(newState: ViewState): Unit = newState match
      case MainMenu =>
        viewState = newState
        mainStage.scene = ControllerMainMenu.scene(
          ActionEvent => this.changeScene(MatchInit), 
          ActionEvent => this.changeScene(MainMenu)
        )
      case MatchInit => 
        viewState = newState
        mainStage.scene = MatchInitScene(ControllerMatchInit)
      
    override def getViewState: ViewState = viewState
  def apply(): ControllerStage = new ControllerStageImpl
