package model.effects

import model.resource.*
import model.utils.ResourceEffectModule
import model.utils.ResourceEffectModules.AddResource

trait Effect:
  def resolve(): Unit

trait CarriesResource extends Effect:
  def getResource: Resource

case class ResourceEffect(resource: Resource,
                          var receiver: Option[PlayerBoard] = Option.empty,
                          private var module: ResourceEffectModule = AddResource) extends CarriesResource:
  
  override def resolve(): Unit =
    receiver match
      case Some(rec) => module.apply(rec, resource)
      case _ => //TODO

  def setReceiver(receiver: PlayerBoard): Unit = this.receiver = Option(receiver)

  def setModule(mod: ResourceEffectModule): Unit = module = mod

  override def getResource: Resource = resource

class OptionEffect(val options: List[Resource]) extends ResourceEffect(Gold(0)):
  var _resource: Resource = super.getResource

  override def resolve(): Unit =
    _resource = options.head  //  scelta
    super.copy(_resource).resolve()

  override def getResource: Resource = _resource

class CopyEffect extends CarriesResource:
  override def resolve(): Unit = {}
  override def getResource: Resource = Gold(0)

class MultiplyEffect(multiplier: Int) extends ResourceEffect(Gold(0)):
  private var _resource: Resource = super.getResource

  def resource_=(resource: Resource): Unit = _resource = resource * (multiplier - 1)
  override def resolve(): Unit = super.copy(_resource).resolve()
  override def getResource: Resource = _resource