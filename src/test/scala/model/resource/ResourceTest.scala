package model.resource

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.language.postfixOps

class ResourceTest extends AnyFlatSpec with Matchers:
  "A resource's current amount" should "not be lower than 0" in :
    var anyResource = ResourceFactory.gold(3)
    val sameResourceDecrease = ResourceFactory.gold(4)

    anyResource.currentAmount should be(3)
    anyResource = anyResource - sameResourceDecrease
    anyResource.currentAmount should be(0)

  "A resource's current amount" should "not be bigger than the max capacity" in:
    var anyResource = ResourceFactory.sunCrystal(0)
    val sameResourceIncrease = ResourceFactory.sunCrystal(4)

    anyResource.currentAmount should be(0)
    anyResource = anyResource + sameResourceIncrease
    anyResource.currentAmount should be(4)
    anyResource = anyResource + sameResourceIncrease
    anyResource.currentAmount should be(6)

  "A resource's max capacity" can "be updated" in:
    var anyResource = ResourceFactory.moonCrystal(4)

    anyResource = anyResource * 2
    anyResource.currentAmount should be(6)
    anyResource = anyResource.updateMaxCapacity(9)
    anyResource = anyResource + anyResource
    anyResource.currentAmount should be(9)

  "When updated, a resource's max capacity" should "still be bigger than 0, otherwise the update is ignored" in:
    var anyResource = ResourceFactory.sunCrystal(4)

    anyResource = anyResource * 2
    anyResource.currentAmount should be(6)
    anyResource = anyResource.updateMaxCapacity(0)
    anyResource.currentAmount should be(6)

  "When created, a player's board" should "have each resource set to 0" in:
    val playerResources = ResourceBoard.emptyBoard

    playerResources.gold.currentAmount should be(0)
    playerResources.sunCrystals.currentAmount should be(0)
    playerResources.moonCrystals.currentAmount should be(0)
    playerResources.victoryPoints.currentAmount should be(0)
