package model.resource

import model.resource.Resource.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.language.postfixOps

class ResourceTestSuite extends AnyFunSuite {
  test("Resource subtraction") {
    assert(Gold(4) - Gold(3) == Gold(1))
    assert(Gold(4) - Gold(5) == Gold(-1))
    assert(Gold(4) - Gold(4) == Gold(0))
  }

  test("Resource addition") {
    assert(Gold(4) + Gold(3) == Gold(7))
    assert(Gold(-3) + Gold(5) == Gold(2))
    assert(Gold(-4) + Gold(4) == Gold(0))
  }

  test("Resource unapply") {
    val goldRes: Resource = Gold(2)
    val gpRes: Resource = GloryPoint(2)
    val sunRes: Resource = SunCrystal(2)
    val moonRes: Resource = MoonCrystal(2)
    assert(unapply(goldRes).contains(2))
    assert(unapply(gpRes).contains(2))
    assert(unapply(sunRes).contains(2))
    assert(unapply(moonRes).contains(2))
  }
}


class ResourceTestSpec extends AnyFlatSpec with Matchers:
  "A resource subtraction" should "not work with a different subtype of Resource" in:
    var anyResource: Resource = Gold(3)
    anyResource = anyResource - SunCrystal(2)
    anyResource should be(Gold(3))
    anyResource = anyResource - MoonCrystal(5)
    anyResource should be(Gold(3))
    anyResource = anyResource - GloryPoint(3)
    anyResource should be(Gold(3))
    anyResource = anyResource - Gold(1)
    anyResource should be(Gold(2))

//  "A resource's current amount" should "not be bigger than the max capacity" in:
//    var anyResource = Resource.sunCrystal(0)
//    val sameResourceIncrease = Resource.sunCrystal(4)
//
//    anyResource.currentAmount should be(0)
//    anyResource = anyResource + sameResourceIncrease
//    anyResource.currentAmount should be(4)
//    anyResource = anyResource + sameResourceIncrease
//    anyResource.currentAmount should be(6)

//  "A resource's max capacity" can "be updated" in:
//    var anyResource = Resource.moonCrystal(4)
//
//    anyResource = anyResource * 2
//    anyResource.currentAmount should be(6)
//    anyResource = anyResource.withUpdatedCapacity(9)
//    anyResource = anyResource + anyResource
//    anyResource.currentAmount should be(9)

//  "When updated, a resource's max capacity" should "still be bigger than 0, otherwise the update is ignored" in:
//    var anyResource = Resource.sunCrystal(4)
//
//    anyResource = anyResource * 2
//    anyResource.currentAmount should be(6)
//    anyResource = anyResource.withUpdatedCapacity(0)
//    anyResource.currentAmount should be(6)

//  "When created, a player's board" should "have each resource set to 0" in:
//    val playerResources = ResourceBoard.emptyBoard
//
//    playerResources.gold.currentAmount should be(0)
//    playerResources.sunCrystals.currentAmount should be(0)
//    playerResources.moonCrystals.currentAmount should be(0)
//    playerResources.victoryPoints.currentAmount should be(0)
