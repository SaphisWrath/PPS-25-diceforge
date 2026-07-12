package model.effects

import model.resource.*

trait Effect:
  def resolve(): Unit
  def setReceiver(receiver: PlayerBoard): Unit

case class ResourceEffect(resource: Resource, var receiver: Option[PlayerBoard] = Option.empty) extends Effect:
  override def resolve(): Unit = receiver match
    case Some(rec) => rec.addResource(resource)
    case _ =>

  override def setReceiver(receiver: PlayerBoard): Unit = this.receiver = Option(receiver)
