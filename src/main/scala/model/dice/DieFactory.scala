package model.dice

import model.effects.Target.Self
import model.effects.*
import model.resource.{Gold, SunCrystal}
import model.utils.RandomModule

object DieFactory:
  private def oneFaceDie(effect: Effect): Die =
    val die = Die(1)
    die.addFace(effect)
    die

  def mockGoldDie: Die =
    oneFaceDie(ResourceEffect(Gold(2), Self))

  def mockCopyDie: Die =
    oneFaceDie(CopyEffect())

  def mockOptionDie: Die =
    oneFaceDie(OptionEffect(Seq(ResourceEffect(Gold(3), Self), ResourceEffect(SunCrystal(2), Self))))

  def mockMultiplyDie: Die =
    oneFaceDie(MultiplyEffect(3))