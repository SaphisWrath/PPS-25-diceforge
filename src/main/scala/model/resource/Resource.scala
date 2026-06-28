package model.resource

object ResourceType:
  type Gold
  type SunCrystal
  type MoonCrystal
  type VictoryPoint

trait Resource[A]:
  def currentAmount: Int
  def updateMaxCapacity(maxCapacity: Int): Unit
  def increase(resource: Resource[A]): Unit
  def decrease(resource: Resource[A]): Unit

class ResourceImpl[A](private val initialAmount: Int, var capacity: Option[Int]) extends Resource[A]:
  var currentAmount: Int = initialAmount

  override def updateMaxCapacity(maxCapacity: Int): Unit =
    capacity = Some(maxCapacity)

  override def increase(resource: Resource[A]): Unit =
    val amount = resource.currentAmount
    currentAmount = currentAmount + amount
    if capacity.isDefined then currentAmount = math.min(currentAmount, capacity.get)

  override def decrease(resource: Resource[A]): Unit =
    val amount = resource.currentAmount
    currentAmount = currentAmount - amount
    currentAmount = math.max(0, currentAmount)
