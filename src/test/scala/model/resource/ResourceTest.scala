package model.resource

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.language.postfixOps

class ResourceTest extends AnyFlatSpec with Matchers:
  "A resource's current amount" should "not be lower than 0" in :
    val anyResource = ResourceFactoryImpl.gold(3)
    val sameResourceDecrease = ResourceFactoryImpl.gold(4)

    anyResource.currentAmount should be(3)
    anyResource.decrease(sameResourceDecrease)
    anyResource.currentAmount should be(0)

  "A resource's current amount" should "not be bigger than the max capacity" in:
    val anyResource = ResourceFactoryImpl.sunCrystal(0)
    val sameResourceIncrease = ResourceFactoryImpl.sunCrystal(4)

    anyResource.currentAmount should be(0)
    anyResource.increase(sameResourceIncrease)
    anyResource.currentAmount should be(4)
    anyResource.increase(sameResourceIncrease)
    anyResource.currentAmount should be(6)

  "A resource's max capacity" can "be updated" in:
    val anyResource = ResourceFactoryImpl.moonCrystal(4)

    anyResource.increase(anyResource)
    anyResource.currentAmount should be(6)
    anyResource.updateMaxCapacity(9)
    anyResource.increase(anyResource)
    anyResource.currentAmount should be(9)

  "When updated, a resource's max capacity" should "still be bigger than 0, otherwise the update is ignored" in:
    val anyResource = ResourceFactoryImpl.sunCrystal(4)

    anyResource.increase(anyResource)
    anyResource.currentAmount should be(6)
    anyResource.updateMaxCapacity(0)
    anyResource.currentAmount should be(6)

  "When created, a player's resources" should "have an amount of 0 each" in:
    val playerResources = PlayerResourcesImpl()

    playerResources.gold.currentAmount should be(0)
    playerResources.sunCrystals.currentAmount should be(0)
    playerResources.moonCrystals.currentAmount should be(0)
    playerResources.victoryPoints.currentAmount should be(0)

  "A player's resources" can "be updated all at once" in:
    val playerResources = PlayerResourcesImpl()
    val resourcesToAdd = List(
        ResourceFactoryImpl.gold(4),
        ResourceFactoryImpl.sunCrystal(2),
        ResourceFactoryImpl.moonCrystal(3),
        ResourceFactoryImpl.victoryPoint(1)
    )

    playerResources.increaseResources(resourcesToAdd)

    playerResources.gold.currentAmount should be(4)
    playerResources.sunCrystals.currentAmount should be(2)
    playerResources.moonCrystals.currentAmount should be(3)
    playerResources.victoryPoints.currentAmount should be(1)
