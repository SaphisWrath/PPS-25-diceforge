package model.dice

import model.resource.PlayerResources

trait Effect:
  def effectResources: PlayerResources
  def applyResourceChange(playerResources: PlayerResources,
    fun: (PlayerResources, PlayerResources) => PlayerResources = _ + _): PlayerResources

case class ResourceEffect(effectResources: PlayerResources) extends Effect:
  override def applyResourceChange(playerResources: PlayerResources,
                                  fun: (PlayerResources, PlayerResources) => PlayerResources): PlayerResources =
    fun(playerResources, effectResources)

trait NonFixedEffect extends Effect:
  def setCurrentEffect(effect: ResourceEffect): Unit

case class MultiplierEffect(multiplier: Int) extends NonFixedEffect:
  private var currentEffect: Effect = ResourceEffect(PlayerResources.setResources(0,0,0,0))

  override def setCurrentEffect(effect: ResourceEffect): Unit =
    currentEffect = ResourceEffect(effect.effectResources * multiplier)

  override def effectResources: PlayerResources =
    currentEffect.effectResources

  override def applyResourceChange(playerResources: PlayerResources,
                                   fun: (PlayerResources, PlayerResources) => PlayerResources): PlayerResources =
    currentEffect.applyResourceChange(playerResources)

class CopyEffect extends MultiplierEffect(1)
