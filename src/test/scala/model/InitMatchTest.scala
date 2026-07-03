package model

import model.Players.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.language.postfixOps

class InitMatchTest extends AnyFlatSpec with Matchers:
  "A MatchBuilder" should "keep track of every added Player" in:
    var matchBuilder: MatchBuilder = MatchBuilderImpl(2)
    val player1 = Player("Mario", Color.Orange)
    matchBuilder = matchBuilder.addPlayer(player1)
    matchBuilder.currentPlayers.size should be(1)

    val player2 = Player("Luigi", Color.Green)
    matchBuilder = matchBuilder.addPlayer(player2)
    matchBuilder.currentPlayers.size should be(2)

  "A Match with 2 Players" should "be 9 Rounds long" in:
    val player1 = Player("Mario", Color.Orange)
    val player2 = Player("Luigi", Color.Green)

    val gameMatch = MatchBuilderImpl(2)
      .addPlayer(player1)
      .addPlayer(player2)
      .build

    gameMatch.remainingRounds should be(9)

  "A Match with 3 Players" should "be 10 Rounds long" in:
    val player1 = Player("Mario", Color.Orange)
    val player2 = Player("Luigi", Color.Green)
    val player3 = Player("Toad", Color.Blue)

    val gameMatch = MatchBuilderImpl(3)
      .addPlayer(player1)
      .addPlayer(player2)
      .addPlayer(player3)
      .build

    gameMatch.remainingRounds should be(10)

  "The player currently acting" should "change after passing their turn" in:
    val player1 = Player("Mario", Color.Orange)
    val player2 = Player("Luigi", Color.Green)

    val gameMatch = MatchBuilderImpl(2)
      .addPlayer(player1)
      .addPlayer(player2)
      .build

    val firstPlayer = gameMatch.getCurrentPlayer
    gameMatch.passTurn()
    gameMatch.getCurrentPlayer should not be(firstPlayer)

  "After every player passes their turn once, the rounds left" should "decrease by 1" in:
    val player1 = Player("Mario", Color.Orange)
    val player2 = Player("Luigi", Color.Green)

    val gameMatch = MatchBuilderImpl(2)
      .addPlayer(player1)
      .addPlayer(player2)
      .build

    gameMatch.remainingRounds should be(9)
    gameMatch.passTurn()
    gameMatch.remainingRounds should be(9)
    gameMatch.passTurn()
    gameMatch.remainingRounds should be(8)