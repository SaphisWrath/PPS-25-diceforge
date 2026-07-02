package model.dice

import model.resource.ResourceBoard

trait Effect[A]:
  def effect: A

case class ResourceEffect(effect: ResourceBoard) extends Effect[ResourceBoard]

trait NonFixedEffect[A] extends Effect[A]:
  def setCurrentEffect(effect: Effect[A]): Unit

class MultiplierEffect(multiplier: Int) extends NonFixedEffect[ResourceBoard]:
  private var currentEffect: Effect[ResourceBoard] = ResourceEffect(ResourceBoard.board(0,0,0,0))

  override def setCurrentEffect(effect: Effect[ResourceBoard]): Unit =
    currentEffect = ResourceEffect(effect.effect * multiplier)

  override def effect: ResourceBoard = currentEffect.effect

class CopyEffect extends MultiplierEffect(1)

case class GrantFaceEffect(effect: Face) extends Effect[Face]