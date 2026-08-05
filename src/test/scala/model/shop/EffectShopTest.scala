package model.shop

import mock.MockPlayer
import model.Players.Color.Orange
import model.effects.ResourceEffect
import model.effects.Target.Self
import model.resource.{Gold, PlayerBoard, SunCrystal}
import org.scalatest.flatspec.AnyFlatSpec

class EffectShopTest extends AnyFlatSpec:
  val mockPlayer = MockPlayer("Bruno", Orange)
  
  "EffectShop" should "return the correct price of an item if it is available" in:
    val item = ResourceEffect(SunCrystal(10), Self)
    val price = Gold(10)
    var shop = EffectShop((item, price))
    assert(shop.getPrice(item).contains(price))
    shop = EffectShop()
    assert(shop.getPrice(item).isEmpty)