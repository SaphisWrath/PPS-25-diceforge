package model.utils.parsers

import io.circe.generic.auto.*
import io.circe.{Json, ParsingFailure, yaml}
import model.resource.*
import model.utils.parsers.Props.ResourceProp

trait YAMLParser[T]:
  def parseYAML(text: String): Either[String, T]
  
object YAMLParsers:
  def parse[T: YAMLParser](text: String): Either[String, T] = summon[YAMLParser[T]].parseYAML(text)

  given YAMLParser[Resource] with
    override def parseYAML(text: String): Either[String, Resource] =
      val json: Either[ParsingFailure, Json] = yaml.parser.parse(text)
      json.fold(
        l => Left("Could not parse into json"),
        r => r.as[ResourceProp].getOrElse(ResourceProp("failed", 1)).toResource
      )