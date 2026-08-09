package model.shop

import mock.MockPlayer
import model.Players.Color.Orange
import model.effects.{CopyEffect, ResourceEffect}
import model.effects.Target.Self
import model.resource.{Gold, PlayerBoard, SunCrystal}
import org.scalatest.flatspec.AnyFlatSpec

class EffectShopTest extends AnyFlatSpec:
  val item = ResourceEffect(SunCrystal(10), Self)
  val price = Gold(10)

  "EffectShop" should "return the correct price of an item if present" in:
    val shop = EffectShop((item, price, 1))
    assert(shop.getPrice(item).contains(price))
    assert(shop.getPrice(CopyEffect()).isEmpty)
    val emptyShop = EffectShop()
    assert(shop.getPrice(CopyEffect()).isEmpty)

  "EffectShop" should "return the correct stocked quantity of an item" in:
    val shop = EffectShop()
    assert(shop.getStocked(item).isEmpty)
    val fullShop = EffectShop((item, price, 2))
    assert(fullShop.getStocked(item).contains(2))
    assert(fullShop.getStocked(CopyEffect()).isEmpty)

  "EffectShop" should "remove an item from inventory if it is bought" in:
    val mockPlayer = MockPlayer("Bruno", Orange)
    val maxGoldAmount = mockPlayer.board.gold.maxCapacity - 1
    val shop = EffectShop((item, Gold(maxGoldAmount / 2), 2))
    mockPlayer.board = PlayerBoard(maxGoldAmount + 1, 0, 0, 0)
    assert(shop.getStocked(item).contains(2))
    shop.buy(item, mockPlayer)
    assert(shop.getStocked(item).contains(1))
    shop.buy(item, mockPlayer)
    assert(shop.getStocked(item).contains(0))

  "EffectShop" should "return false if conditions to buy (necessary funds and item in stock) are not met" in:
    val mockPlayer = MockPlayer("Bruno", Orange)
    var shop = EffectShop()
    assert(!shop.buy(item, mockPlayer))
    shop = EffectShop((item, price, 1))
    mockPlayer.board = PlayerBoard.emptyBoard
    assert(!shop.buy(item, mockPlayer))