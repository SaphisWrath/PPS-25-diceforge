package model.effects

import model.resource.*
import model.utils.ResourceEffectModule
import model.utils.ResourceEffectModules.AddResource

trait Effect:
  def resolve(): Unit

case class ResourceEffect(resource: Resource, var receiver: Option[PlayerBoard] = Option.empty) extends Effect:
  private var module: ResourceEffectModule = AddResource

  override def resolve(): Unit =
    receiver match
      case Some(rec) => module.apply(rec, resource)
      case _ => //TODO

  def setReceiver(receiver: PlayerBoard): Unit = this.receiver = Option(receiver)

  def setModule(mod: ResourceEffectModule): Unit = module = mod
