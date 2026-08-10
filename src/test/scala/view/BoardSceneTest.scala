package view

import _root_.mock.MockControllerStage
import controller.GameController
import controller.ViewState.Board
import model.GameMatch
import model.Players.Color.{Blue, Green, Orange}
import model.Players.Player
import scalafx.application.JFXApp3
import view.scenes.BoardScene

object BoardSceneTest extends JFXApp3:

  override def start(): Unit = {
    val gameMatch = GameMatch(Seq(Player("Paul", Orange), Player("Paulo", Blue), Player("Pietro", Green)))
    val controller = GameController(gameMatch)
    stage = TestStageSetup(BoardScene(controller, MockControllerStage(Board))).stage
  }

