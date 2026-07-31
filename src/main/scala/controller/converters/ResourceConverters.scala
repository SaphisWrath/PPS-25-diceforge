package controller.converters

import model.resource.*
import view.LanguageStrings.{separator, ResourceStrings as Strings}

object ResourceConverters:

  def resourceToString(resource: Resource): String = resource match
    case Gold(_) => Strings.gold
    case SunCrystal(_) => Strings.sunCrystal
    case MoonCrystal(_) => Strings.moonCrystal
    case GloryPoint(_) => Strings.gloryPoint
    case _ => throw IllegalArgumentException(s"$resource is not a recognized resource")

  def resourceWithAmountToString(resource: Resource): String = resource match
    case Resource(amount) => resourceToString(resource) + separator + amount.toString

  def stringToResourceBuilder(string: String)(amount: Int): Resource = string match
    case Strings.gold => Gold(amount)
    case Strings.sunCrystal => SunCrystal(amount)
    case Strings.moonCrystal => MoonCrystal(amount)
    case Strings.gloryPoint => GloryPoint(amount)
    case _ => throw IllegalArgumentException(s"`$string` doesn't correspond to any recognized resource")

