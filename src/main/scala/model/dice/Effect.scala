package model.dice

import model.resource.*

/**
 * A wrapper that represents an effect triggered by a die face, a mission, or something else
 *
 * @tparam A the return type of the effect
 */
trait Effect[A]:
  def effect: A

case class ResourceEffect(effect: List[Resource]) extends Effect[List[Resource]]
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
class MultiplierEffect(multiplier: Int) extends NonFixedEffect[List[Resource]]:
  private var currentEffect = ResourceEffect(List.empty)

  override def setCurrentEffect(effect: Effect[List[Resource]]): Unit =
    currentEffect = ResourceEffect(effect.effect.map(_ * multiplier))

  override def effect: List[Resource] = currentEffect.effect

class CopyEffect extends MultiplierEffect(1)
