package model.dice

import model.effects.*
import model.effects.Target.Self
import model.resource.{GloryPoint, Gold, MoonCrystal, SunCrystal}

object DieFactory:
  private def oneFaceDie(effect: Effect): Die =
    val die = Die(1)
    die.addFace(effect)
    die

  private def makeStarterDie1(): Die =
    val goldEffect = ResourceEffect(Gold(1), Self)
    Die(Seq(
      goldEffect,
      goldEffect,
      goldEffect,
      goldEffect,
      goldEffect,
      ResourceEffect(SunCrystal(1), Self)
    ))

  private def makeStarterDie2(): Die =
    val goldEffect = ResourceEffect(Gold(1), Self)
    Die(Seq(
      goldEffect,
      goldEffect,
      goldEffect,
      goldEffect,
      ResourceEffect(MoonCrystal(1), Self),
      ResourceEffect(GloryPoint(2), Self)
    ))

  def makeStarterDice: Seq[Die] =
    Seq(
      makeStarterDie1(),
      makeStarterDie2()
    )
  
  def mockGoldDie: Die =
    oneFaceDie(ResourceEffect(Gold(2), Self))

  def mockCopyDie: Die =
    oneFaceDie(CopyEffect())

  def mockOptionDie: Die =
    oneFaceDie(OptionEffect(Seq(ResourceEffect(Gold(3), Self), ResourceEffect(SunCrystal(2), Self))))

  def mockMultiplyDie: Die =
    oneFaceDie(MultiplyEffect(3))