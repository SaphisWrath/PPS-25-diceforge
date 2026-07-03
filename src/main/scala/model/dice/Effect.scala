package model.dice

import model.resource.ResourceBoard

/**
 * A wrapper that represents an effect triggered by a die face, a mission, or something else
 *
 * @tparam A the return type of the effect
 */
trait Effect[A]:
  def effect: A

case class ResourceEffect(effect: ResourceBoard) extends Effect[ResourceBoard]
case class GrantFaceEffect(effect: Face) extends Effect[Face]

/**
 * An effect not determined right away that can change during the match
 * Behavior of effect prior to setting one is undefined
 *
 * @tparam A the return type of the effect
 */
trait NonFixedEffect[A] extends Effect[A]:
  def setCurrentEffect(effect: Effect[A]): Unit

/**
 * An effect that, upon setting a resource effect, multiplies the resources to add by a specified factor
 *
 * @param multiplier the factor to multiply the inner effect by
 */
class MultiplierEffect(multiplier: Int) extends NonFixedEffect[ResourceBoard]:
  private var currentEffect: Effect[ResourceBoard] = ResourceEffect(ResourceBoard.board(0,0,0,0))

  override def setCurrentEffect(effect: Effect[ResourceBoard]): Unit =
    currentEffect = ResourceEffect(effect.effect * multiplier)

  override def effect: ResourceBoard = currentEffect.effect

class CopyEffect extends MultiplierEffect(1)
