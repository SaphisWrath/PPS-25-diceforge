package controller.converters

import model.resource.*

object ResourceConverters:
  val GoldString = "Oro"
  val SunCrystalString = "Cristalli Solari"
  val MoonCrystalString = "Cristalli Lunari"
  val GloryPointString = "Punti Gloria"

  def resourceToString(resource: Resource): String = resource match
    case Gold(_) => GoldString
    case SunCrystal(_) => SunCrystalString
    case MoonCrystal(_) => MoonCrystalString
    case GloryPoint(_) => GloryPointString
    case _ => throw IllegalArgumentException(s"$resource is not a recognized resource")

  def stringToResourceBuilder(string: String)(amount: Int):Resource =string match
    case GoldString => Gold(amount)
    case SunCrystalString => SunCrystal(amount)
    case MoonCrystalString => MoonCrystal(amount)
    case GloryPointString => GloryPoint(amount)
    case _ => throw IllegalArgumentException(s"`$string` doesn't correspond to any recognized resource")

