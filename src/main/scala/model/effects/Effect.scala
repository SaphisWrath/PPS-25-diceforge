package model.effects

import model.Players.Player
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

case class ResourceEffect(resource: Resource, target: Target) extends Effect:
  private var module: ResourceEffectModule = AddResource

  override def resolve(receiver: Player): Unit = module.apply(receiver.board, resource)

  def setModule(mod: ResourceEffectModule): Unit = module = mod
