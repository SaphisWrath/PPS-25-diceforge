package view

import controller.{ControllerStage, GameController}
import model.Players.Color.{Blue, Orange}
import model.Players.Player
import org.mockito.Mockito.*
import scalafx.application.JFXApp3
import view.scenes.BoardScene

object BoardSceneTest extends JFXApp3:
  private val controllerStage: ControllerStage = mock[ControllerStage]()
  
  override def start(): Unit = {
    val controller = GameController(Seq(Player("Paul", Orange), Player("Paulo", Blue)))
    stage = TestStageSetup(BoardScene(controller, controllerStage)).stage
  }

