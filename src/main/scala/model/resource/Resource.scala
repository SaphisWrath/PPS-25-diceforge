package model.resource

trait ResourceType

class Gold extends ResourceType
class SunCrystal extends ResourceType
class MoonCrystal extends ResourceType
class VictoryPoint extends ResourceType

trait Resource[A <: ResourceType]:
  def currentAmount: Int
  def maxCapacity: Option[Int]

case class ResourceImpl[A <: ResourceType](currentAmount: Int, maxCapacity: Option[Int]) extends Resource[A]

object Resource:

  private def resourceCheck[A <: ResourceType](res: Resource[A]): Resource[A] =
    var amount = res.currentAmount
    if res.maxCapacity.isDefined
    then amount = math.min(res.currentAmount, res.maxCapacity.get)
    amount = math.max(amount, 0)
    ResourceImpl[A](amount, res.maxCapacity)

  extension [A <: ResourceType](first: Resource[A])
    def +(other: Resource[A]): Resource[A] =
      resourceCheck(ResourceImpl[A](first.currentAmount + other.currentAmount, first.maxCapacity))

    def -(other: Resource[A]): Resource[A] =
      resourceCheck(ResourceImpl[A](first.currentAmount - other.currentAmount, first.maxCapacity))

    def *(multiplier: Int): Resource[A] =
      resourceCheck(ResourceImpl[A](first.currentAmount * multiplier, first.maxCapacity))

    def updateMaxCapacity(newMaxCapacity: Int): Resource[A] =
      if newMaxCapacity > 0
      then resourceCheck(ResourceImpl[A](first.currentAmount, Some(newMaxCapacity)))
      else first

object ResourceFactory:
  def gold(amount: Int): Resource[Gold] = ResourceImpl[Gold](amount, Some(12))
  def sunCrystal(amount: Int): Resource[SunCrystal] = ResourceImpl[SunCrystal](amount, Some(6))
  def moonCrystal(amount: Int): Resource[MoonCrystal] = ResourceImpl[MoonCrystal](amount, Some(6))
  def victoryPoint(amount: Int): Resource[VictoryPoint] = ResourceImpl[VictoryPoint](amount, Option.empty)

case class PlayerResources(gold: Resource[Gold],
                           sunCrystals: Resource[SunCrystal],
                           moonCrystals: Resource[MoonCrystal],
                           victoryPoints: Resource[VictoryPoint])

object PlayerResources:
  def setPlayerResources(gold: Int,
                         sunCrystals: Int,
                         moonCrystals: Int,
                         victoryPoints: Int): PlayerResources =
    PlayerResources(ResourceFactory.gold(gold),
      ResourceFactory.sunCrystal(sunCrystals),
      ResourceFactory.moonCrystal(moonCrystals),
      ResourceFactory.victoryPoint(victoryPoints))

  def emptyPlayerResources: PlayerResources =
    setPlayerResources(0,0,0,0)