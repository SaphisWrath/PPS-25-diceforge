package model.effects

import mock.MockPlayer
import model.Players.Color.{Black, Blue, Orange}
import model.dice.Die
import model.effects.Target.Self
import model.effects.ThrowEffects.{CopyOtherThrowResults, PlainThrowEffect, ThrowSubtractEffect, ThrowTimesEffect}
import model.resource.{Gold, PlayerBoard}
import org.scalatest.flatspec.AnyFlatSpec

class ThrowEffectTest extends AnyFlatSpec:
  private def newPlayers: Seq[MockPlayer] = Seq(MockPlayer("Bruno", Orange), MockPlayer("Mark", Blue), MockPlayer("August", Black))

  "A ThrowSubtractEffect" should "subtract instead of giving resources to everyone" in:
    val players: Seq[MockPlayer] = newPlayers
    val res = Gold(2)
    val throws = 2
    val mockDie = Die(Seq(ResourceEffect(res, Self)))
    val expected: Array[Int] = Array.ofDim(players.length)
    players.zipWithIndex.foreach((p, i) =>
      p.dice = Seq(mockDie, mockDie)
      p.board = PlayerBoard.emptyBoard
      p.board.addResource(res.copy(res.amount * 5))
      expected(i) = math.max(p.board.gold.amount - (res.amount * 2 * throws), 0)
    )
    ThrowSubtractEffect(throws).resolve(players)
    players.zipWithIndex.foreach((p, i) =>
      assert(p.board.gold.amount == expected(i))
    )

  "A ThrowTimesEffect" should "throw the dice the specified amount of times" in:
    val players: Seq[MockPlayer] = newPlayers
    val res = Gold(1)
    val throws = 2
    val mockDie = Die(Seq(ResourceEffect(res, Self)))
    val expected: Array[Int] = Array.ofDim(players.length)
    players.zipWithIndex.foreach((p, i) =>
      p.dice = Seq(mockDie, mockDie)
      p.board = PlayerBoard.emptyBoard
      expected(i) =  math.min(p.board.gold.amount + (res.amount * 2 * throws), p.board.gold.maxCapacity)
    )
    ThrowTimesEffect(throws).resolve(players)
    players.zipWithIndex.foreach((p, i) =>
      assert(p.board.gold.amount == expected(i))
    )

  "A PlainThrowEffect" should "let players throw their dice without getting the rewards from them" in:
    val resEffect = ResourceEffect(Gold(3), Self)
    val mockDie = Die(Seq(resEffect))
    val players = newPlayers
    newPlayers.foreach(p =>
      p.dice = Seq(mockDie, mockDie)
      p.board = PlayerBoard.emptyBoard
    )
    PlainThrowEffect().resolve(players)
    players.foreach(p =>
      assert(p.board.gold.amount == 0)
      p.dice.foreach(d => assert(d.lastRolledEffect.isDefined))
    )

  "A CopyOtherThrowResults effect" should "let the player copy other player's results twice" in:
    val resEffect = ResourceEffect(Gold(1), Self)
    val mockDie = Die(Seq(resEffect))
    val activePlayer = MockPlayer("Bruno", Orange)
    val otherPlayers = Seq(MockPlayer("Lau", Orange))
    activePlayer.board = PlayerBoard.emptyBoard
    otherPlayers.foreach(_.dice = Seq(mockDie, mockDie))
    PlainThrowEffect().resolve(otherPlayers)
    CopyOtherThrowResults(Self).resolve(Seq(activePlayer))

    assert(EffectManager().effectsToSolve.size == 2)
    EffectManager().effectsToSolve.foreach((p, e) =>
      assert(p.name == activePlayer.name)
      assert(e.effects.contains(resEffect))
    )