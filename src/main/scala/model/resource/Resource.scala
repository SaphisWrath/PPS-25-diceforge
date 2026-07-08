package model.resource

trait Resource:
  def amount: Int

case class Gold(amount: Int) extends Resource
case class SunCrystal(amount: Int) extends Resource
case class MoonCrystal(amount: Int) extends Resource
case class GloryPoint(amount: Int) extends Resource

object Resource:
  def unapply(resource: Resource): Option[Int] =
    Some(resource.amount)

  extension (r1: Resource)
    private def applyFun(r2: Resource, fun: (Int, Int) => Int): Resource =
      val positiveFun: (Int, Int) => Int = (first, second) => math.max(fun(first, second), 0)

      (r1, r2) match
        case (Gold(amount1), Gold(amount2)) => Gold(positiveFun(amount1, amount2))
        case (SunCrystal(amount1), SunCrystal(amount2)) => SunCrystal(positiveFun(amount1, amount2))
        case (MoonCrystal(amount1), MoonCrystal(amount2)) => MoonCrystal(positiveFun(amount1, amount2))
        case (GloryPoint(amount1), GloryPoint(amount2)) => GloryPoint(positiveFun(amount1, amount2))

    def +(r2: Resource): Resource = r1.applyFun(r2, _ + _)
    def -(r2: Resource): Resource = r1.applyFun(r2, _ - _)
    def *(multiplier: Int): Resource =
      if multiplier > 1 then r1 + (r1 * (multiplier - 1)) else r1

trait ResourceWithCap extends Resource:
  def maxCapacity: Int
  def maxCapacity_=(newCapacity: Int): Unit
  def resource: Resource

object ResourceWithCap:
  private class ResourceWithCapImpl(var resource: Resource, initCapacity: Int) extends ResourceWithCap:
    private var _maxCapacity = initCapacity
    override def maxCapacity: Int = _maxCapacity
    override def maxCapacity_=(newCapacity: Int): Unit =
      if newCapacity > 0
      then
        resource = resource match
          case Gold(_) => Gold(this.amount)
          case SunCrystal(_) => SunCrystal(this.amount)
          case MoonCrystal(_) => MoonCrystal(this.amount)
          case GloryPoint(_) => GloryPoint(this.amount)
        _maxCapacity = newCapacity

    override def amount: Int = math.min(resource.amount, _maxCapacity)

  def apply(resource: Resource, initCapacity: Int): ResourceWithCap =
    ResourceWithCapImpl(resource, initCapacity)

  extension (r1: ResourceWithCap)
    def +(r2: Resource): ResourceWithCap = ResourceWithCap(r1.resource + r2, r1.maxCapacity)
    def -(r2: Resource): ResourceWithCap = ResourceWithCap(r1.resource - r2, r1.maxCapacity)
