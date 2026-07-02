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

case class ResourceBoard(gold: Resource[Gold],
                         sunCrystals: Resource[SunCrystal],
                         moonCrystals: Resource[MoonCrystal],
                         victoryPoints: Resource[VictoryPoint])

object ResourceBoard:
  extension [A <: ResourceType](first: ResourceBoard)
    private def applyFun(other: ResourceBoard, fun: (Int, Int) => Int): ResourceBoard =
      first match
        case ResourceBoard(g, s, m, v) => ResourceBoard(
          ResourceFactory.gold(fun(g.currentAmount, other.gold.currentAmount), g.maxCapacity),
          ResourceFactory.sunCrystal(fun(s.currentAmount, other.sunCrystals.currentAmount), s.maxCapacity),
          ResourceFactory.moonCrystal(fun(m.currentAmount, other.moonCrystals.currentAmount), m.maxCapacity),
          ResourceFactory.victoryPoint(fun(v.currentAmount, other.victoryPoints.currentAmount), v.maxCapacity)
        )

    def +(other: ResourceBoard): ResourceBoard =
      first.applyFun(other, _ + _)

    def -(other: ResourceBoard): ResourceBoard =
      first.applyFun(other, _ - _)

    def *(multiplier: Int): ResourceBoard =
      first match
        case ResourceBoard(g, s, m, v) => ResourceBoard(
          g * multiplier,
          s * multiplier,
          m * multiplier,
          v * multiplier
        )
  
  def board(gold: Int,
            sunCrystals: Int,
            moonCrystals: Int,
            victoryPoints: Int): ResourceBoard =
    ResourceBoard(ResourceFactory.gold(gold),
      ResourceFactory.sunCrystal(sunCrystals),
      ResourceFactory.moonCrystal(moonCrystals),
      ResourceFactory.victoryPoint(victoryPoints))

  def emptyBoard: ResourceBoard =
    board(0,0,0,0)