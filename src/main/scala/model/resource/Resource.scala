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
  capacity = capacity.filter(_ > 0)
  amountCheck()

  override def updateMaxCapacity(maxCapacity: Int): Unit =
    if maxCapacity > 0
    then
      capacity = Some(maxCapacity)
      amountCheck()

  override def increase(resource: Resource[A]): Unit =
    amountChange(resource.currentAmount)

  override def decrease(resource: Resource[A]): Unit =
    amountChange(-resource.currentAmount)

  private def amountChange(change: Int): Unit =
    currentAmount = currentAmount + change
    amountCheck()

  private def amountCheck(): Unit =
    currentAmount = math.max(0, currentAmount)
    if capacity.isDefined then currentAmount = math.min(currentAmount, capacity.get)
