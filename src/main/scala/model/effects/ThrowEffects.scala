package model.effects

import model.Players.Player
import model.utils.RandomModules.given_RandomModule_Int
import model.utils.ResourceEffectModules.SubtractResource

trait ThrowAction:
  protected def results: Seq[(Player, Effect)]
  def throwDice(receiver: Player): Unit
  def throwDice(receivers: Seq[Player]): Unit

trait SubtractThrow extends ThrowAction:
  abstract override def throwDice(receiver: Player): Unit = {
    super.throwDice(receiver)
    results.foreach((p, e) => e match
      case r: ResourceEffect => r.setModule(SubtractResource)
    )
  }

object ThrowEffects:
  class ThrowAllDice extends Effect with ThrowAction:
    protected var results: Seq[(Player, Effect)] = Seq.empty

    override def throwDice(receiver: Player): Unit =
      results = results.concat(receiver.dice.map(d => (receiver, d.roll)))
    

    override def resolve(receiver: Player): Unit =
      throwDice(receiver)
      EffectManager().attemptSolve(results)

    override def resolve(receivers: Seq[Player]): Unit =
      throwDice(receivers)
    
    override def throwDice(receivers: Seq[Player]): Unit =
      receivers.foreach(throwDice)
      EffectManager().attemptSolve(results)

  class ThrowSubtractEffect extends ThrowAllDice with SubtractThrow