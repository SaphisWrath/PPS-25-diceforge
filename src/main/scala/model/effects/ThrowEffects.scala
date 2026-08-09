package model.effects

import model.ModelPublisher.ModelContext.DieChoiceContext
import model.Players.Player
import model.{ModelPublisher, effects}
import model.utils.RandomModules.given_RandomModule_Int
import model.utils.ResourceEffectModules.SubtractResource

trait ThrowAction:
  protected def results: Seq[(Player, Effect, Int)]
  def throwDice(receiver: Player): Unit
  def throwDice(receivers: Seq[Player]): Unit

trait SubtractThrow extends ThrowAction:
  abstract override def throwDice(receiver: Player): Unit =
    super.throwDice(receiver)
    results.foreach((_, e, _) => e match
      case r: ResourceEffect => r.setModule(SubtractResource)
    )

object ThrowEffects:
  class ThrowAllDice(times: Int = 1) extends Effect with ThrowAction:
    protected var results: Seq[(Player, Effect, Int)] = Seq.empty

    override def throwDice(receiver: Player): Unit =
      results = results.concat(receiver.dice.zipWithIndex.map((d, i) => (receiver, d.roll, i)))

    override def resolve(receiver: Player): Unit =
      resolve(Seq(receiver))

    override def resolve(receivers: Seq[Player]): Unit =
      Array.range(0, times).foreach(_ =>
        throwDice(receivers)
        EffectManager().updateTurnEffects(results)
        EffectManager().attemptSolve(results.map((p, e, i) => (p, e)))
        results = Seq.empty
      )

    override def throwDice(receivers: Seq[Player]): Unit =
      receivers.foreach(throwDice)

  class ThrowSubtractEffect extends ThrowAllDice with SubtractThrow
  class ThrowTimesEffect(times: Int = 1) extends ThrowAllDice(times)

  class ThrowOneDie(times: Int = 1) extends Effect:
    override def resolve(receiver: Player): Unit =
      receiver.pendingRolls = times
      ModelPublisher().notify(DieChoiceContext)