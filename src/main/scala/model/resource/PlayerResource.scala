package model.resource

import model.resource.*

trait PlayerResource:
  def maximumCapacity: Int
  def resource: Resource
  def setMaxAmount(newAmount: Int): Unit
  def setResource(newResource: Resource): Unit

object PlayerResource:
  private class PlayerResourceImpl(var maximumCapacity: Int,
                                   var resource: Resource) 
  extends PlayerResource {
    override def setMaxAmount(newAmount: Int): Unit =
      maximumCapacity = if newAmount >= 1 then newAmount else maximumCapacity

    override def setResource(newResource: Resource): Unit = newResource match
      case Resource(n) if n >= 0 & n <= maximumCapacity => resource = newResource
  }
  
  def apply(maximumCapacity: Int, resource: Resource): PlayerResource = PlayerResourceImpl(maximumCapacity, resource)
  def unapply(playerResource: PlayerResource): Option[(Int, Resource)] =
    Some(playerResource.maximumCapacity, playerResource.resource)