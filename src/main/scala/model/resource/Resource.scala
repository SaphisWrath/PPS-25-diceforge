package model.resource

/**
 * A resource that can be obtained and used in game by the player
 */
trait Resource:
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
  def increase(resource: Resource): Unit

  /**
   * A method that decreases the amount of the resource
   * @param resource the resource of matching type to be subtracted
   */
  def decrease(resource: Resource): Unit

class ResourceDecorator(plainResource: Resource) extends Resource:
  override def currentAmount: Int = plainResource.currentAmount
  override def updateMaxCapacity(maxCapacity: Int): Unit = plainResource.updateMaxCapacity(maxCapacity)
  override def increase(resource: Resource): Unit = plainResource.increase(resource)
  override def decrease(resource: Resource): Unit = plainResource.decrease(resource)

class Gold(plainResource: Resource) extends ResourceDecorator(plainResource: Resource)
class SunCrystal(plainResource: Resource) extends ResourceDecorator(plainResource: Resource)
class MoonCrystal(plainResource: Resource) extends ResourceDecorator(plainResource: Resource)
class VictoryPoint(plainResource: Resource) extends ResourceDecorator(plainResource: Resource)

/**
 * A factory that creates all types of resources, each one with its own capacity
 */
trait ResourceFactory:
  def gold(amount: Int): Gold
  def sunCrystal(amount: Int): SunCrystal
  def moonCrystal(amount: Int): MoonCrystal
  def victoryPoint(amount: Int): VictoryPoint

object ResourceFactoryImpl extends ResourceFactory:
  private class ResourceImpl(private val initialAmount: Int, var capacity: Option[Int]) extends Resource:
    var currentAmount: Int = initialAmount
    capacity = capacity.filter(_ > 0)
    amountCheck()

    override def updateMaxCapacity(maxCapacity: Int): Unit =
      if maxCapacity > 0
      then
        capacity = Some(maxCapacity)
        amountCheck()

    override def increase(resource: Resource): Unit =
      amountChange(resource.currentAmount)

    override def decrease(resource: Resource): Unit =
      amountChange(-resource.currentAmount)

    private def amountChange(change: Int): Unit =
      currentAmount = currentAmount + change
      amountCheck()

    private def amountCheck(): Unit =
      currentAmount = math.max(0, currentAmount)
      if capacity.isDefined then currentAmount = math.min(currentAmount, capacity.get)

  override def gold(amount: Int): Gold =
    Gold(ResourceImpl(amount, Some(12)))

  override def sunCrystal(amount: Int): SunCrystal =
    SunCrystal(ResourceImpl(amount, Some(6)))

  override def moonCrystal(amount: Int): MoonCrystal =
    MoonCrystal(ResourceImpl(amount, Some(6)))

  override def victoryPoint(amount: Int): VictoryPoint =
    VictoryPoint(ResourceImpl(amount, Option.empty))

trait PlayerResources:
  def gold: Gold
  def sunCrystals: SunCrystal
  def moonCrystals: MoonCrystal
  def victoryPoints: VictoryPoint

  def increaseResources(incResources: List[Resource]): Unit
  def decreaseResources(decResources: List[Resource]): Unit

class PlayerResourcesImpl extends PlayerResources:
  val gold: Gold = ResourceFactoryImpl.gold(0)
  val sunCrystals: SunCrystal = ResourceFactoryImpl.sunCrystal(0)
  val moonCrystals: MoonCrystal = ResourceFactoryImpl.moonCrystal(0)
  val victoryPoints: VictoryPoint = ResourceFactoryImpl.victoryPoint(0)

  override def increaseResources(incResources: List[Resource]): Unit = ???

  override def decreaseResources(decResources: List[Resource]): Unit = ???