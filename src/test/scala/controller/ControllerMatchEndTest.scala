package controller

import model.Players.Color.*
import model.Players.Player
import model.resource.{GloryPoint, PlayerBoard}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ControllerMatchEndTest extends AnyFlatSpec with Matchers:
  "The controller" should "correctly sort the players from best performance to worst" in:
    val respectiveBoards = List(
      PlayerBoard(Player("Mario", Orange), 0, 0, 0, 60),
      PlayerBoard(Player("Luigi", Green), 0, 0, 0, 110),
      PlayerBoard(Player("Toad", Blue), 0, 0, 0, 85)
    )

    val controller = ControllerMatchEndImpl(respectiveBoards.map(board => (board.player, GloryPoint(board.gloryPoints.amount))))
    controller.getSortedPlayers.map(_._1.getName) should be(List("Luigi", "Toad", "Mario"))