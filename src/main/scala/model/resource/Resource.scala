package model.resource

import model.resource.ResourceType.{Gold, MoonCrystal, SunCrystal, VictoryPoint}

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

trait ResourceFactory:
  def gold(amount: Int, capacity: Option[Int]): Resource[ResourceType.Gold]
  def sunCrystal(amount: Int, capacity: Option[Int]): Resource[ResourceType.SunCrystal]
  def moonCrystal(amount: Int, capacity: Option[Int]): Resource[ResourceType.MoonCrystal]
  def victoryPoint(amount: Int, capacity: Option[Int]): Resource[ResourceType.VictoryPoint]

object ResourceFactoryImpl extends ResourceFactory:
  override def gold(amount: Int, capacity: Option[Int]): Resource[Gold] =
    ResourceImpl[Gold](amount, capacity)

  override def sunCrystal(amount: Int, capacity: Option[Int]): Resource[SunCrystal] =
    ResourceImpl[SunCrystal](amount, capacity)

  override def moonCrystal(amount: Int, capacity: Option[Int]): Resource[MoonCrystal] =
    ResourceImpl[MoonCrystal](amount, capacity)

  override def victoryPoint(amount: Int, capacity: Option[Int]): Resource[VictoryPoint] =
    ResourceImpl[VictoryPoint](amount, capacity)
