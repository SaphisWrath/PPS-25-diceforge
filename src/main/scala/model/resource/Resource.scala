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
  def gold(amount: Int, cap: Option[Int] = Some(12)): Resource[Gold] =
    ResourceImpl[Gold](amount, cap)
  def sunCrystal(amount: Int, cap: Option[Int] = Some(6)): Resource[SunCrystal] =
    ResourceImpl[SunCrystal](amount, cap)
  def moonCrystal(amount: Int, cap: Option[Int] = Some(6)): Resource[MoonCrystal] =
    ResourceImpl[MoonCrystal](amount, cap)
  def victoryPoint(amount: Int, cap: Option[Int] = Option.empty): Resource[VictoryPoint] =
    ResourceImpl[VictoryPoint](amount, cap)

case class PlayerResources(gold: Resource[Gold],
                           sunCrystals: Resource[SunCrystal],
                           moonCrystals: Resource[MoonCrystal],
                           victoryPoints: Resource[VictoryPoint])

object PlayerResources:
  extension [A <: ResourceType](first: PlayerResources)
    private def applyFun(other: PlayerResources, fun: (Int, Int) => Int): PlayerResources =
      first match
        case PlayerResources(g, s, m, v) => PlayerResources(
          ResourceFactory.gold(fun(g.currentAmount, other.gold.currentAmount), g.maxCapacity),
          ResourceFactory.sunCrystal(fun(s.currentAmount, other.sunCrystals.currentAmount), s.maxCapacity),
          ResourceFactory.moonCrystal(fun(m.currentAmount, other.moonCrystals.currentAmount), m.maxCapacity),
          ResourceFactory.victoryPoint(fun(v.currentAmount, other.victoryPoints.currentAmount), v.maxCapacity)
        )

    def +(other: PlayerResources): PlayerResources =
      first.applyFun(other, _ + _)

    def -(other: PlayerResources): PlayerResources =
      first.applyFun(other, _ - _)

    def *(multiplier: Int): PlayerResources =
      first match
        case PlayerResources(g, s, m, v) => PlayerResources(
          g * multiplier,
          s * multiplier,
          m * multiplier,
          v * multiplier
        )
  
  def setResources(gold: Int,
                   sunCrystals: Int,
                   moonCrystals: Int,
                   victoryPoints: Int): PlayerResources =
    PlayerResources(ResourceFactory.gold(gold),
      ResourceFactory.sunCrystal(sunCrystals),
      ResourceFactory.moonCrystal(moonCrystals),
      ResourceFactory.victoryPoint(victoryPoints))

  def emptyPlayerResources: PlayerResources =
    setResources(0,0,0,0)