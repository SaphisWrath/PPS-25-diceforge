package model.effects

import mock.MockPlayer
import model.Players.Color.{Black, Blue, Orange}
import model.dice.Die
import model.effects.Target.Self
import model.effects.ThrowEffects.{ThrowSubtractEffect, ThrowTimesEffect}
import model.resource.{Gold, PlayerBoard}
import org.scalatest.flatspec.AnyFlatSpec

class ThrowEffectTest extends AnyFlatSpec:
  private def newPlayers: Seq[MockPlayer] = Seq(MockPlayer("Bruno", Orange), MockPlayer("Mark", Blue), MockPlayer("August", Black))

  "A ThrowSubtractEffect" should "subtract instead of giving resources to everyone" in:
    val players: Seq[MockPlayer] = newPlayers
    val res = Gold(2)
    val mockDie = Die(Seq(ResourceEffect(res, Self)))
    val expected: Array[Int] = Array.ofDim(players.length)
    players.zipWithIndex.foreach((p, i) =>
      p.dice = Seq(mockDie, mockDie)
      p.board.addResource(res.copy(res.amount * 5))
      expected(i) = math.max(p.board.gold.amount - (res.amount * 2), 0)
    )
    ThrowSubtractEffect().resolve(players)
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