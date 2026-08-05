package model.shop

import mock.MockPlayer
import model.Players.Color.Orange
import model.effects.ResourceEffect
import model.effects.Target.Self
import model.resource.{Gold, PlayerBoard, SunCrystal}
import org.scalatest.flatspec.AnyFlatSpec

class EffectShopTest extends AnyFlatSpec:
  val mockPlayer = MockPlayer("Bruno", Orange)
  val item = ResourceEffect(SunCrystal(10), Self)
  val price = Gold(10)
  
  "EffectShop" should "return the correct price of an item regardless of availability" in:
    var shop = EffectShop((item, price))
    assert(shop.getPrice(item) == price)
    shop = EffectShop()
    assert(shop.getPrice(item) == price)
    
  "EffectShop" should "remove an item from inventory if it is bought" in:
    val shop = EffectShop((item, price), (item, price))
    mockPlayer.board = PlayerBoard(price.amount * 3, 0, 0, 0)
    assert(shop.items.length == 2)
    shop.buy(item, mockPlayer)
    assert(shop.items.length == 1)
    shop.buy(item, mockPlayer)
    assert(shop.items.isEmpty)