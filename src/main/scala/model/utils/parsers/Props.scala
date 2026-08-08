package model.utils.parsers

import model.resource.{GloryPoint, Gold, MoonCrystal, Resource, SunCrystal}

object Props:
  case class ResourceProp(rType: String, amount: Int):
    def toResource: Either[String, Resource] = rType match
      case s if s == "gold" => Right(Gold(amount))
      case s if s == "sun" => Right(SunCrystal(amount))
      case s if s == "moon" => Right(MoonCrystal(amount))
      case s if s == "glory" => Right(GloryPoint(amount))
      case s if s == "failed" => Left("Error in YAML parsing of resource.")
      case _ => Left("Failed to match parsed resource.")
      
  