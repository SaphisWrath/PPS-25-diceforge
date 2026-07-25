package controller

import controller.ViewState.*

enum ViewState:
  case MainMenu
  case MatchInit
  case Board
  case MatchEnd

trait ControllerStage:
  def changeScene(newState: ViewState): Unit
  def getViewState: ViewState

object ControllerStage:
  private class ControllerStageImpl(navigator: Navigator) extends ControllerStage:
    private var viewState: ViewState = MainMenu
    changeScene(MainMenu)

    override def changeScene(newState: ViewState): Unit =
      newState match
        case MainMenu => navigator.navigateToMainMenu()
        case MatchInit => navigator.navigateToMatchInit()
        case Board => navigator.navigateToBoard()
        case MatchEnd => navigator.navigateToMatchEnd()
      viewState = newState
    override def getViewState: ViewState = viewState

  def apply(navigator: Navigator): ControllerStage = ControllerStageImpl(navigator)
