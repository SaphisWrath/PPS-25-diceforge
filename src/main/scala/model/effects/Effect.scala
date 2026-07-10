package model.effects

import model.resource.*

trait Effect:
  def resolve(): Unit

class ResourceEffect(resource: Resource, receiver: PlayerBoard) extends Effect:
  override def resolve(): Unit = receiver.addResource(resource)
