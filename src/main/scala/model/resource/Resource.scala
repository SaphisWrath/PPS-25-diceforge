package model.resource

object ResourceType:
  type Gold
  type SunCrystal
  type MoonCrystal
  type VictoryPoint

/**
 * A resource that can be obtained and used in game by the player
 * @tparam A the type of resource, should be under ResourceType
 */
trait Resource[A]:
  def currentAmount: Int

  /**
   * A method that updates the capacity of the resource,
   * it should check the current amount after the change is applied
   * @param maxCapacity the new max capacity for the resource
   */
  def updateMaxCapacity(maxCapacity: Int): Unit

  /**
   * A method that increases the amount of the resource
   * @param resource the resource of matching type to be added
   */
  def increase(resource: Resource[A]): Unit

  /**
   * A method that decreases the amount of the resource
   * @param resource the resource of matching type to be subtracted
   */
  def decrease(resource: Resource[A]): Unit

import model.resource.ResourceType.*

/**
 * A factory that creates all types of resources, each one with its own capacity
 */
trait ResourceFactory:
  def gold(amount: Int): Resource[Gold]
  def sunCrystal(amount: Int): Resource[SunCrystal]
  def moonCrystal(amount: Int): Resource[MoonCrystal]
  def victoryPoint(amount: Int): Resource[VictoryPoint]

object ResourceFactoryImpl extends ResourceFactory:
  private class ResourceImpl[A](private val initialAmount: Int, var capacity: Option[Int]) extends Resource[A]:
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

  override def gold(amount: Int): Resource[Gold] =
    ResourceImpl[Gold](amount, Some(12))

  override def sunCrystal(amount: Int): Resource[SunCrystal] =
    ResourceImpl[SunCrystal](amount, Some(6))

  override def moonCrystal(amount: Int): Resource[MoonCrystal] =
    ResourceImpl[MoonCrystal](amount, Some(6))

  override def victoryPoint(amount: Int): Resource[VictoryPoint] =
    ResourceImpl[VictoryPoint](amount, Option.empty)
