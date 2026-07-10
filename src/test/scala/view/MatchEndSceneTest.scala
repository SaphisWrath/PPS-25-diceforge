package view

import controller.ControllerMatchEndImpl
import model.Players.Color.*
import model.Players.Player
import model.resource.{GloryPoint, PlayerBoard}
import scalafx.application.JFXApp3
import view.TestStageSetup
import view.scenes.MatchEndScene

object MatchEndSceneTest extends JFXApp3:
  override def start(): Unit = {
    val players = List(Player("Mario", Orange), Player("Luigi", Green), Player("Toad", Blue))
    val respectiveBoards = List(
      PlayerBoard(0, 0, 0, 60),
      PlayerBoard(0, 0, 0, 110),
      PlayerBoard(0, 0, 0, 85)
    )

    val controller = ControllerMatchEndImpl(players.zip(respectiveBoards.map(board => GloryPoint(board.gloryPoints.amount))))
    stage = TestStageSetup(MatchEndScene(controller)).stage
  }