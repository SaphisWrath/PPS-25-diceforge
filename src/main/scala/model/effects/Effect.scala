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

class OptionEffect(val options: List[Resource]) extends ResourceEffect(Gold(0), Self)

class CopyEffect extends ResourceEffect(Gold(0), Self)

class MultiplyEffect(multiplier: Int) extends ResourceEffect(Gold(0), Self):
  private val emptyResource = Gold(0)
  private var _resource: Resource = emptyResource

  def resource_=(resource: Resource): Unit = _resource = resource
  override def resolve(player: Player): Unit =
    super.copy(_resource * (multiplier - 1), Self).resolve(player)