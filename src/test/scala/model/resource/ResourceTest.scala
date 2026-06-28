package model.resource

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.language.postfixOps

class ResourceTest extends AnyFlatSpec with Matchers:
  "A resource's current amount" should "not be lower than 0" in :
    val anyResource: Resource[ResourceType.Gold] = ResourceImpl[ResourceType.Gold](3, Option.empty)
    val sameResourceDecrease: Resource[ResourceType.Gold] = ResourceImpl[ResourceType.Gold](4, Option.empty)

    anyResource.currentAmount should be(3)
    anyResource.decrease(sameResourceDecrease)
    anyResource.currentAmount should be(0)

  "A resource's current amount" should "not be bigger than the max capacity" in:
    val anyResource: Resource[ResourceType.SunCrystal] = ResourceImpl[ResourceType.SunCrystal](0, Some(6))
    val sameResourceIncrease: Resource[ResourceType.SunCrystal] = ResourceImpl[ResourceType.SunCrystal](4, Some(6))

    anyResource.currentAmount should be(0)
    anyResource.increase(sameResourceIncrease)
    anyResource.currentAmount should be(4)
    anyResource.increase(sameResourceIncrease)
    anyResource.currentAmount should be(6)

  "A resource's max capacity" should "be bigger than 0, ignored otherwise" in:
    val anyResource: Resource[ResourceType.MoonCrystal] = ResourceImpl[ResourceType.MoonCrystal](2, Some(-3))

    anyResource.currentAmount should be(2)
    anyResource.increase(anyResource)
    anyResource.currentAmount should be(4)

  "A resource's max capacity" can "be updated" in:
    val anyResource: Resource[ResourceType.MoonCrystal] = ResourceImpl[ResourceType.MoonCrystal](4, Some(6))

    anyResource.increase(anyResource)
    anyResource.currentAmount should be(6)
    anyResource.updateMaxCapacity(9)
    anyResource.increase(anyResource)
    anyResource.currentAmount should be(9)

  "When updated, a resource's max capacity" should "still be bigger than 0, otherwise the update is ignored" in:
    val anyResource: Resource[ResourceType.SunCrystal] = ResourceImpl[ResourceType.SunCrystal](4, Some(6))

    anyResource.increase(anyResource)
    anyResource.currentAmount should be(6)
    anyResource.updateMaxCapacity(0)
    anyResource.currentAmount should be(6)
