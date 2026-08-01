package controller

import model.GameMatch
import model.Players.Player
import model.effects.Target.Self
import model.effects.ResourceEffect
import model.utils.{DiceThrow, TemporaryDie}

type PlayerChoice[A] = (Player, Seq[A])
object PlayerChoice:
  def apply[A](player: Player, options: Seq[A]): PlayerChoice[A] = (player, options)

trait DiceThrowManager:
  def copyEffectsFromRoll(dice: Seq[(Player, Seq[TemporaryDie])]): Seq[PlayerChoice[ResourceEffect]]
  def optionEffectsFromRoll(solvedCopyEffects: Seq[(Player, ResourceEffect)]): Seq[PlayerChoice[ResourceEffect]]
  def endRoll(solvedOptionEffects: Seq[(Player, ResourceEffect)]): Unit
  def allRawEffects: Seq[(Player, ResourceEffect)]

object DiceThrowManager:
  private class DiceThrowManagerImpl(gameMatch: GameMatch) extends DiceThrowManager:
    private val diceThrowHelper = DiceThrow(gameMatch)
    var allRawEffects: Seq[(Player, ResourceEffect)] = Seq.empty

    override def copyEffectsFromRoll(dice: Seq[(Player, Seq[TemporaryDie])]): Seq[PlayerChoice[ResourceEffect]] =
      val (copyEffects, otherEffects) = diceThrowHelper.initiateDiceRoll(dice)
      allRawEffects = copyEffects.concat(otherEffects)
      copyEffects.map((p, e) =>
        PlayerChoice(p, otherEffects.flatMap((otherP, otherE) => if otherP == p then Seq.empty else Seq(otherE)))
      )

    override def optionEffectsFromRoll(solvedCopyEffects: Seq[(Player, ResourceEffect)]): Seq[PlayerChoice[ResourceEffect]] =
      diceThrowHelper
        .sortEffects(solvedCopyEffects)
        .map((p, e) => PlayerChoice(p, e.options.map(option => ResourceEffect(option, Self))))

    override def endRoll(solvedOptionEffects: Seq[(Player, ResourceEffect)]): Unit =
      diceThrowHelper.resolveAll(solvedOptionEffects)

  def apply(gameMatch: GameMatch): DiceThrowManager = DiceThrowManagerImpl(gameMatch)