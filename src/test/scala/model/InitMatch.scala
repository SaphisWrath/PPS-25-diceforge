package model

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.language.postfixOps

class InitMatch extends AnyFlatSpec with Matchers:
  "A Player" should "have a unique Name" in:
    val playerFactory = PlayerFactoryImpl()
    val player1 = playerFactory.create("Mario", Color.Orange)
    val player2 = playerFactory.create("Mario", Color.Green)

    player2 should be(Option.empty)

  "A Player" should "have a unique Color" in:
    val playerFactory = PlayerFactoryImpl()
    val player1 = playerFactory.create("Mario", Color.Orange)
    val player2 = playerFactory.create("Luigi", Color.Orange)

    player2 should be(Option.empty)

  "A MatchBuilder" should "be ready only after adding every Player" in:
    var matchBuilder: MatchBuilder = MatchBuilderImpl(2)
    val playerFactory = PlayerFactoryImpl()
    val player1 = playerFactory.create("Mario", Color.Orange)
    matchBuilder = matchBuilder.addPlayer(player1.get)
    matchBuilder.ready should be(false)

    val player2 = playerFactory.create("Luigi", Color.Green)
    matchBuilder = matchBuilder.addPlayer(player2.get)
    matchBuilder.ready should be(true)

  "A Match with 2 Players" should "be 9 Rounds long" in:
    val playerFactory = PlayerFactoryImpl()
    val player1 = playerFactory.create("Mario", Color.Orange)
    val player2 = playerFactory.create("Luigi", Color.Green)

    val gameMatch = MatchBuilderImpl(2)
      .addPlayer(player1.get)
      .addPlayer(player2.get)
      .build

    gameMatch.remainingRounds should be(9)

  "A Match with 3 Players" should "be 10 Rounds long" in:
    val playerFactory = PlayerFactoryImpl()
    val player1 = playerFactory.create("Mario", Color.Orange)
    val player2 = playerFactory.create("Luigi", Color.Green)
    val player3 = playerFactory.create("Toad", Color.Blue)

    val gameMatch = MatchBuilderImpl(3)
      .addPlayer(player1.get)
      .addPlayer(player2.get)
      .addPlayer(player3.get)
      .build

    gameMatch.remainingRounds should be(10)

  "The player currently acting" should "change after passing their turn" in:
    val playerFactory = PlayerFactoryImpl()
    val player1 = playerFactory.create("Mario", Color.Orange)
    val player2 = playerFactory.create("Luigi", Color.Green)

    val gameMatch = MatchBuilderImpl(2)
      .addPlayer(player1.get)
      .addPlayer(player2.get)
      .build

    val firstPlayer = gameMatch.getCurrentPlayer
    gameMatch.passTurn()
    gameMatch.getCurrentPlayer should not be (firstPlayer)

  "After every player passes their turn once, the rounds left" should "decrease by 1" in:
    val playerFactory = PlayerFactoryImpl()
    val player1 = playerFactory.create("Mario", Color.Orange)
    val player2 = playerFactory.create("Luigi", Color.Green)

    val gameMatch = MatchBuilderImpl(2)
      .addPlayer(player1.get)
      .addPlayer(player2.get)
      .build

    gameMatch.remainingRounds should be(9)
    gameMatch.passTurn()
    gameMatch.remainingRounds should be(9)
    gameMatch.passTurn()
    gameMatch.remainingRounds should be(8)