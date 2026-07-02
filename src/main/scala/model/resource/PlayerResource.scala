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
    resource = resource match
      case Gold(_) => Gold(0)
      case SunCrystal(_) => SunCrystal(0)
      case MoonCrystal(_) => MoonCrystal(0)
      case GloryPoint(_) => GloryPoint(0)
    
    override def setMaxAmount(newAmount: Int): Unit =
      maximumCapacity = if newAmount >= 1 then newAmount else 1

    override def setResource(newResource: Resource): Unit = newResource match
      case Resource(n) if n >= 0 & n <= maximumCapacity => resource = newResource
      case Resource(n) if n > maximumCapacity => resource match {
        case Gold(_) => resource = Gold(maximumCapacity)
        case SunCrystal(_) => resource = SunCrystal(maximumCapacity)
        case MoonCrystal(_) => resource = MoonCrystal(maximumCapacity)
        case GloryPoint(_) => resource = GloryPoint(maximumCapacity)
      }
      case _ => resource match {
        case Gold(_) => resource = Gold(0)
        case SunCrystal(_) => resource = SunCrystal(0)
        case MoonCrystal(_) => resource = MoonCrystal(0)
        case GloryPoint(_) => resource =  GloryPoint(0)
      }
  }
  
  def apply(maximumCapacity: Int, resource: Resource): PlayerResource = PlayerResourceImpl(maximumCapacity, resource)
  def unapply(playerResource: PlayerResource): Option[(Int, Resource)] =
    Some(playerResource.maximumCapacity, playerResource.resource)