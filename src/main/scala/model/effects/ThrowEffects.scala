package model.effects

import model.ModelPublisher.ModelContext.{DieChoiceContext, ResourceContext}
import model.ModelPublisher.ModelSubscriber
import model.Players.Player
import model.effects.Target.Self
import model.{ModelPublisher, effects}
import model.utils.RandomModules.given_RandomModule_Int
import model.utils.ResourceEffectModules.SubtractResource

trait ThrowAction:
  protected def results: Seq[(Player, Effect, Int)]
  def throwDice(receiver: Player): Unit
  def throwDice(receivers: Seq[Player]): Unit = receivers.foreach(throwDice)

trait SubtractThrow extends ThrowAction:
  abstract override def throwDice(receiver: Player): Unit =
    super.throwDice(receiver)
    EffectManager().setModuleOnce(SubtractResource)
    results.foreach((_, e, _) => e match
      case r: ResourceEffect => r.setModule(SubtractResource)
    )

object ThrowEffects:
  protected abstract class PartialThrowEffect extends Effect with ThrowAction:
    protected var results: Seq[(Player, Effect, Int)] = Seq.empty

    override def throwDice(receiver: Player): Unit =
      results = results.concat(receiver.dice.zipWithIndex.map((d, i) => (receiver, d.roll, i)))

    override def resolve(receiver: Player): Unit = resolve(Seq(receiver))

  case class ThrowAllDice(times: Int = 1, override val target: Target = Self) extends PartialThrowEffect with ModelSubscriber:
    this.setPublisher(ModelPublisher())
    private var currentPlayers: Seq[Player] = Seq.empty
    private var pendingCount = 0

    private def rollDice(): Unit =
      results = Seq.empty
      throwDice(currentPlayers)
      EffectManager().updateTurnEffects(results)
      EffectManager().attemptSolve(results.map((p, e, i) => (p, e)))

    override def resolve(receivers: Seq[Player]): Unit =
      pendingCount = times - 1
      currentPlayers = receivers
      rollDice()

    override def update(context: ModelPublisher.ModelContext): Unit = context match
      case ResourceContext =>
        if currentPlayers.nonEmpty && pendingCount > 0
        then
          pendingCount = pendingCount - 1
          rollDice()
      case _ =>

  class ThrowSubtractEffect(times: Int = 1, target: Target = Self) extends ThrowAllDice(times, target) with SubtractThrow
  class ThrowTimesEffect(times: Int = 1, target: Target = Self) extends ThrowAllDice(times, target)

  class ThrowOneDie(val times: Int = 1) extends Effect:
    override def resolve(receiver: Player): Unit =
      receiver.pendingRolls = times
      ModelPublisher().notify(DieChoiceContext)

  case class PlainThrowEffect(override val target: Target = Self) extends PartialThrowEffect:
    override def resolve(receivers: Seq[Player]): Unit =
      results = Seq.empty
      throwDice(receivers)
      EffectManager().updateTurnEffects(results)

  case class CopyOtherThrowResults(override val target: Target) extends Effect:
    override def resolve(receiver: Player): Unit =
      EffectManager().attemptSolve(
        LazyList
          .continually((receiver, CopyEffect()))
          .take(2)
      )