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