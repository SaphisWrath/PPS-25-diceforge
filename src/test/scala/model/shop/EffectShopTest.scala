package model.shop

import mock.MockPlayer
import model.Players.Color.Orange
import model.effects.ResourceEffect
import model.effects.Target.Self
import model.resource.{Gold, PlayerBoard, SunCrystal}
import org.scalatest.flatspec.AnyFlatSpec

class EffectShopTest extends AnyFlatSpec:
  val item = ResourceEffect(SunCrystal(10), Self)
  val price = Gold(10)

  "EffectShop" should "return the correct price of an item" in:
    val shop = EffectShop((item, price))
    assert(shop.getPrice(item) == price)

  "EffectShop" should "throw an exception if the price catalog is empty" in:
    val shop = EffectShop()
    assertThrows[IllegalStateException](shop.getPrice(item))

  "EffectShop" should "remove an item from inventory if it is bought" in:
    val mockPlayer = MockPlayer("Bruno", Orange)
    val maxGoldAmount = mockPlayer.board.gold.maxCapacity - 1
    val shop = EffectShop((item, Gold(maxGoldAmount / 2)), (item, Gold(maxGoldAmount / 2)))
    mockPlayer.board = PlayerBoard(maxGoldAmount + 1, 0, 0, 0)
    assert(shop.items.length == 2)
    shop.buy(item, mockPlayer)
    assert(shop.items.length == 1)
    shop.buy(item, mockPlayer)
    assert(shop.items.isEmpty)

  "EffectShop" should "throw IllegalStateException if conditions to buy (necessary funds and item in stock) are not met" in:
    val mockPlayer = MockPlayer("Bruno", Orange)
    var shop = EffectShop()
    assertThrows[IllegalStateException](shop.buy(item, mockPlayer))
    shop = EffectShop((item, price))
    mockPlayer.board = PlayerBoard.emptyBoard
    assertThrows[IllegalStateException](shop.buy(item, mockPlayer))