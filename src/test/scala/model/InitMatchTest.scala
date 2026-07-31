package model

import controller.{MatchBuilder, MatchBuilderImpl}
import model.Players.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.language.postfixOps

class InitMatchTest extends AnyFlatSpec with Matchers:
  "A MatchBuilder" should "keep track of every added Player" in :
    var matchBuilder: MatchBuilder = MatchBuilderImpl()
    val player1 = Player("Mario", Color.Orange)
    matchBuilder.addPlayer(player1)
    matchBuilder.currentPlayers.size should be(1)

    val player2 = Player("Luigi", Color.Green)
    matchBuilder.addPlayer(player2)
    matchBuilder.currentPlayers.size should be(2)

  "A Match with 2 Players" should "be 9 Rounds long" in :
    val player1 = Player("Mario", Color.Orange)
    val player2 = Player("Luigi", Color.Green)
    val matchBuilder = MatchBuilderImpl()
    matchBuilder.addPlayer(player1)
    matchBuilder.addPlayer(player2)
    val gameMatch = matchBuilder.build()

    gameMatch.maxNumberOfRounds should be(9)

  "A Match with 3 Players" should "be 10 Rounds long" in :
    val player1 = Player("Mario", Color.Orange)
    val player2 = Player("Luigi", Color.Green)
    val player3 = Player("Toad", Color.Blue)

    val matchBuilder = MatchBuilderImpl()
    matchBuilder.addPlayer(player1)
    matchBuilder.addPlayer(player2)
    matchBuilder.addPlayer(player3)
    val gameMatch = matchBuilder.build()

    gameMatch.maxNumberOfRounds should be(10)