package model.shop.factories

import model.effects.Target.Self
import model.effects.{Effect, OptionEffect, ResourceEffect, SumEffect}
import model.resource.{GloryPoint, Gold, MoonCrystal, SunCrystal}
import model.shop.{EffectShop, Shop}

class EffectShopFactory extends ShopFactory[Effect]:

  override def makeStandardShop: Shop[Effect] =
    EffectShop(
      // 2 Gold
      (ResourceEffect(MoonCrystal(1), Self), Gold(2), 4),
      (ResourceEffect(Gold(3), Self), Gold(2), 4),
      // 3 Gold
      (ResourceEffect(SunCrystal(1), Self), Gold(3), 4),
      (ResourceEffect(Gold(4), Self), Gold(3), 4),
      // 4 Gold
      (SumEffect(Seq(ResourceEffect(GloryPoint(1), Self), ResourceEffect(SunCrystal(1), Self))), Gold(4), 1),
      (ResourceEffect(Gold(6), Self), Gold(4), 1),
      (OptionEffect(Seq(
        ResourceEffect(Gold(1), Self),
        ResourceEffect(MoonCrystal(1), Self),
        ResourceEffect(SunCrystal(1), Self)
      )), Gold(4), 1),
      (SumEffect(Seq(ResourceEffect(Gold(2), Self), ResourceEffect(MoonCrystal(1), Self))), Gold(4), 1),
      // 5 Gold
      (OptionEffect(Seq(
        ResourceEffect(Gold(3), Self),
        ResourceEffect(GloryPoint(2), Self))), Gold(5), 4),
      // 6 Gold
      (ResourceEffect(MoonCrystal(2), Self), Gold(6), 4),
      // 8 Gold
      (ResourceEffect(SunCrystal(2), Self), Gold(8), 4),
      (ResourceEffect(GloryPoint(3), Self), Gold(8), 4),
      // 12 Gold
      (OptionEffect(Seq(
          ResourceEffect(Gold(2), Self),
          ResourceEffect(MoonCrystal(2), Self),
          ResourceEffect(SunCrystal(2), Self)
      )), Gold(12), 1),
      (SumEffect(Seq(
        ResourceEffect(Gold(1), Self),
        ResourceEffect(MoonCrystal(1), Self),
        ResourceEffect(SunCrystal(1), Self),
        ResourceEffect(GloryPoint(1), Self)
      )), Gold(12), 1),
      (SumEffect(Seq(
        ResourceEffect(GloryPoint(2), Self),
        ResourceEffect(MoonCrystal(2), Self),
      )), Gold(12), 1),
      (ResourceEffect(GloryPoint(4), Self), Gold(12), 1)
    )
