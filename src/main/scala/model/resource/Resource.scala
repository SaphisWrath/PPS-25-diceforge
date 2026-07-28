package model.resource

trait Resource:
  def amount: Int
  def copy(amount: Int): Resource

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
      if r1 == r2.copy(r1.amount)
      then r1.copy(positiveFun(r1.amount, r2.amount))
      else r1

    def +(r2: Resource): Resource = r1.applyFun(r2, _ + _)
    def -(r2: Resource): Resource = r1.applyFun(r2, _ - _)
    def *(multiplier: Int): Resource =
      if multiplier > 1 then r1 + (r1 * (multiplier - 1)) else r1

trait ResourceWithCap extends Resource:
  def maxCapacity: Int
  def maxCapacity_=(newCapacity: Int): Unit
  def resource: Resource

object ResourceWithCap:
  private class ResourceWithCapImpl(var _resource: Resource, initCapacity: Int) extends ResourceWithCap:
    private var _maxCapacity = initCapacity
    override def maxCapacity: Int = _maxCapacity
    override def maxCapacity_=(newCapacity: Int): Unit =
      if newCapacity > 0
      then
        _resource = _resource.copy(this.amount)
        _maxCapacity = newCapacity

    override def amount: Int = math.min(_resource.amount, _maxCapacity)
    override def copy(amount: Int): Resource = ResourceWithCapImpl(_resource, _maxCapacity)
    override def resource: Resource = _resource.copy(this.amount)

  def apply(resource: Resource, initCapacity: Int): ResourceWithCap =
    ResourceWithCapImpl(resource, initCapacity)

  extension (r1: ResourceWithCap)
    def +(r2: Resource): ResourceWithCap = ResourceWithCap(r1.resource + r2, r1.maxCapacity)
    def -(r2: Resource): ResourceWithCap = ResourceWithCap(r1.resource - r2, r1.maxCapacity)
