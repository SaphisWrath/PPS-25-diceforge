package model.effects

import model.resource.*
import model.utils.ResourceEffectModule

trait Effect:
  def resolve(using module: ResourceEffectModule)(): Unit
  def setReceiver(receiver: PlayerBoard): Unit

case class ResourceEffect(resource: Resource, var receiver: Option[PlayerBoard] = Option.empty) extends Effect:
  override def resolve(using module: ResourceEffectModule)(): Unit = receiver match
    case Some(rec) => module.apply(rec, resource)
    case _ =>

  override def setReceiver(receiver: PlayerBoard): Unit = this.receiver = Option(receiver)
