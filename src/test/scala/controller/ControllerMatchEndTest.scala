package controller

import model.Players.Color.*
import model.Players.Player
import model.resource.ResourceBoard
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ControllerMatchEndTest extends AnyFlatSpec with Matchers:
  "The controller" should "correctly sort the players from best performance to worst" in:
    val players = List(Player("Mario", Orange), Player("Luigi", Green), Player("Toad", Blue))
    val respectiveBoards = List(
      ResourceBoard.board(0,0,0,60),
      ResourceBoard.board(0,0,0,110),
      ResourceBoard.board(0,0,0,85)
    )

    val controller = ControllerMatchEndImpl(players.zip(respectiveBoards.map(_.victoryPoints)))
    controller.getSortedPlayers.map(_._1.getName) should be(List("Luigi", "Toad", "Mario"))