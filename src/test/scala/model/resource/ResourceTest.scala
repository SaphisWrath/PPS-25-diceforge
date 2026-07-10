package model.resource

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.language.postfixOps

class ResourceTest extends AnyFlatSpec with Matchers:
  "A resource's current amount" should "not be lower than 0" in :
    var anyResource: Resource = Gold(3)
    val sameResourceDecrease = Gold(4)

    anyResource.amount should be(3)
    anyResource = anyResource - sameResourceDecrease
    anyResource.amount should be(0)

  "A resource's current amount" should "not be bigger than the max capacity" in:
    var anyResource = ResourceWithCap(SunCrystal(0), 6)
    val sameResourceIncrease = SunCrystal(4)

    anyResource.amount should be(0)
    anyResource = anyResource + sameResourceIncrease
    anyResource.amount should be(4)
    anyResource = anyResource + sameResourceIncrease
    anyResource.amount should be(6)

  "A resource's max capacity" can "be updated" in:
    var anyResource = ResourceWithCap(MoonCrystal(4), 6)
    val sameResourceIncrease = MoonCrystal(4)

    anyResource = anyResource + sameResourceIncrease
    anyResource.amount should be(6)
    anyResource.maxCapacity = 9
    anyResource.amount should be(6)
    anyResource = anyResource + sameResourceIncrease
    anyResource.amount should be(9)

  "When updated, a resource's max capacity" should "still be bigger than 0, otherwise the update is ignored" in:
    var anyResource = ResourceWithCap(SunCrystal(4), 6)

    anyResource = anyResource + SunCrystal(4)
    anyResource.amount should be(6)
    anyResource.maxCapacity = 0
    anyResource.amount should be(6)

  "When created, a player's board" should "have each resource set to 0" in:
    val playerResources = PlayerBoard.emptyBoard

    playerResources.gold.amount should be(0)
    playerResources.sunCrystals.amount should be(0)
    playerResources.moonCrystals.amount should be(0)
    playerResources.gloryPoints.amount should be(0)
