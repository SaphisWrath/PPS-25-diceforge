package view

import controller.ControllerMatchEndImpl
import model.Players.Color.*
import model.Players.Player
import model.resource.ResourceBoard
import scalafx.application.JFXApp3
import view.TestStageSetup

object MatchEndSceneTest extends JFXApp3:
  override def start(): Unit = {
    val players = List(Player("Mario", Orange), Player("Luigi", Green), Player("Toad", Blue))
    val respectiveBoards = List(
      ResourceBoard.board(0, 0, 0, 60),
      ResourceBoard.board(0, 0, 0, 110),
      ResourceBoard.board(0, 0, 0, 85)
    )

    val controller = ControllerMatchEndImpl(players.zip(respectiveBoards.map(_.victoryPoints)))
    stage = TestStageSetup(MatchEndScene(controller)).stage
  }