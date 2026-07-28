package view

import _root_.mock.MockControllerStage
import controller.GameController
import controller.ViewState.Board
import model.GameMatch
import model.Players.Color.{Blue, Orange}
import model.Players.Player
import scalafx.application.JFXApp3
import view.scenes.BoardScene

object BoardSceneTest extends JFXApp3:

  override def start(): Unit = {
    val controller = GameController(GameMatch(Seq(Player("Paul", Orange), Player("Paulo", Blue))))
    stage = TestStageSetup(BoardScene(controller, MockControllerStage(Board))).stage
  }

