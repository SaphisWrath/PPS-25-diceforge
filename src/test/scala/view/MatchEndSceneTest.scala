package view

import controller.ControllerMatchEndImpl
import model.Players.Color.*
import model.Players.Player
import model.resource.{GloryPoint, PlayerBoard}
import scalafx.application.JFXApp3
import view.TestStageSetup

object MatchEndSceneTest extends JFXApp3:
  override def start(): Unit = {
    val respectiveBoards = List(
      PlayerBoard(Player("Mario", Orange), 0, 0, 0, 60),
      PlayerBoard(Player("Luigi", Green), 0, 0, 0, 110),
      PlayerBoard(Player("Toad", Blue), 0, 0, 0, 85)
    )

    val controller = ControllerMatchEndImpl(respectiveBoards.map(board => (board.player, GloryPoint(board.gloryPoints.amount))))
    stage = TestStageSetup(MatchEndScene(controller)).stage
  }