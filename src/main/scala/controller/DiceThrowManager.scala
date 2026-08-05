package controller

import model.GameMatch
import model.Players.Player
import model.effects.Effect
import model.utils.{DiceThrow, TemporaryDie}

type PlayerChoice[A] = (Player, Seq[A])
object PlayerChoice:
  def apply[A](player: Player, options: Seq[A]): PlayerChoice[A] = (player, options)

trait DiceThrowManager:
  def copyEffectsFromRoll(dice: Seq[(Player, Seq[TemporaryDie])]): Seq[PlayerChoice[Effect]]
  def optionEffectsFromRoll(solvedCopyEffects: Seq[(Player, Effect)]): Seq[PlayerChoice[Effect]]
  def endRoll(solvedOptionEffects: Seq[(Player, Effect)]): Unit
  def allRawEffects: Seq[(Player, Effect)]

object DiceThrowManager:
  private class DiceThrowManagerImpl(gameMatch: GameMatch) extends DiceThrowManager:
    private val diceThrowHelper = DiceThrow(gameMatch)
    var allRawEffects: Seq[(Player, Effect)] = Seq.empty

    override def copyEffectsFromRoll(dice: Seq[(Player, Seq[TemporaryDie])]): Seq[PlayerChoice[Effect]] =
      val (copyEffects, otherEffects) = diceThrowHelper.initiateDiceRoll(dice)
      allRawEffects = copyEffects.concat(otherEffects)
      copyEffects.map((p, e) =>
        PlayerChoice(p, otherEffects.flatMap((otherP, otherE) => if otherP == p then Seq.empty else Seq(otherE)))
      )

    override def optionEffectsFromRoll(solvedCopyEffects: Seq[(Player, Effect)]): Seq[PlayerChoice[Effect]] =
      diceThrowHelper
        .sortEffects(solvedCopyEffects)
        .map((p, e) => PlayerChoice(p, e.options))

    override def endRoll(solvedOptionEffects: Seq[(Player, Effect)]): Unit =
      diceThrowHelper.resolveAll(solvedOptionEffects)

  def apply(gameMatch: GameMatch): DiceThrowManager = DiceThrowManagerImpl(gameMatch)