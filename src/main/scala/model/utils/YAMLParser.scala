package model.utils

import model.resource.{GloryPoint, Gold, MoonCrystal, Resource, SunCrystal}
import io.circe.{Json, ParsingFailure, yaml}
import io.circe.generic.auto.*

trait YAMLParser[T]:
  def parseYAML(text: String): Either[String, T]
  
object YAMLParsers:
  private case class ResourceProp(rType: String, amount: Int):
    def toResource: Either[String, Resource] = rType match
      case s if s == "gold" => Right(Gold(amount))
      case s if s == "sun" => Right(SunCrystal(amount))
      case s if s == "moon" => Right(MoonCrystal(amount))
      case s if s == "glory" => Right(GloryPoint(amount))
      case _ => Left("Failed to match resource")

  def parse[T: YAMLParser](text: String): Either[String, T] = summon[YAMLParser[T]].parseYAML(text)

  given YAMLParser[Resource] with
    override def parseYAML(text: String): Either[String, Resource] =
      val json: Either[ParsingFailure, Json] = yaml.parser.parse(text)
      json.fold(
        l => Left("Could not parse into json"),
        r =>
          r.as[ResourceProp].getOrElse(ResourceProp("failed", 1)).toResource
      )