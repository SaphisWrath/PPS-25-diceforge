package model.effects

import model.Players.Player
import model.effects.Target.Self
import model.resource.*
import model.utils.ResourceEffectModule
import model.utils.ResourceEffectModules.AddResource

enum Target:
  case Self
  case All
  case Others

trait Effect:
  def resolve(receivers: Seq[Player]): Unit = receivers.foreach(resolve)
  def resolve(receiver: Player): Unit

case class ResourceEffect(resource: Resource, target: Target, private var module: ResourceEffectModule = AddResource) extends Effect:
  override def resolve(receiver: Player): Unit = module.apply(receiver.board, resource)
  def setModule(mod: ResourceEffectModule): Unit = module = mod

val emptyEffect = ResourceEffect(Gold(0), Self)

trait EffectWrapper extends Effect:
  def currentEffect: Effect
  def currentEffect_=(effect: Effect): Unit
  override def resolve(receiver: Player): Unit = currentEffect.resolve(receiver)

abstract case class CompoundEffect(effects: Seq[Effect]) extends Effect

object CompoundEffect:
  def unapply(compoundEffect: CompoundEffect): Option[Seq[Effect]] = Some(compoundEffect.effects)

class SumEffect(effects: Seq[Effect]) extends CompoundEffect(effects):
  override def resolve(receiver: Player): Unit = effects.foreach(_.resolve(receiver))

class UpdateCapacityEffect(resource: Resource) extends Effect:
  override def resolve(receiver: Player): Unit = resource match
    case Gold(newMax) => receiver.board.gold.maxCapacity = newMax
    case SunCrystal(newMax) => receiver.board.sunCrystals.maxCapacity = newMax
    case MoonCrystal(newMax) => receiver.board.moonCrystals.maxCapacity = newMax
    case _ =>
    

class OptionEffect(options: Seq[Effect]) extends CompoundEffect(options) with EffectWrapper:
  private var _currentEffect: Effect = options.head
  override def currentEffect: Effect = _currentEffect
  override def currentEffect_=(effect: Effect): Unit = if options.contains(effect) then _currentEffect = effect

class CopyEffect extends Effect with EffectWrapper:
  var currentEffect: Effect = emptyEffect

case class MultiplyEffect(multiplier: Int) extends Effect with EffectWrapper:
  var currentEffect: Effect = emptyEffect
  override def resolve(receiver: Player): Unit =
    LazyList
      .continually(() => currentEffect.resolve(receiver))
      .take(multiplier - 1)
      .foreach(_())
    currentEffect = emptyEffect