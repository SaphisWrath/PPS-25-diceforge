package model.effects

import model.resource.*

trait Effect:
  def resolve(): Unit

case class ResourceEffect(resource: Resource, receiver: Option[PlayerBoard]) extends Effect:
  override def resolve(): Unit = receiver match
    case Some(rec) => rec.addResource(resource)
    case _ => 
