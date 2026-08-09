package model.utils

import model.effects.CopyEffect
import model.resource.{Gold, Resource}
import model.utils.parsers.YAMLParsers.parseParameter
import org.scalatest.flatspec.AnyFlatSpec

class YAMLParserTest extends AnyFlatSpec:
  "parseParameter" should "parse a parameter from correct syntax" in:
    val yaml ="""
      |parType: class
      |value: model.resource.Gold
      |parameters:
      | -
      |   parType: int
      |   value: 1
      |   parameters:
      |""".stripMargin
    val res: Either[String, Any] = parseParameter(yaml)
    assert(res == Right(Gold(1)))

  "parse" should "parse an effect from correct syntax" in:
    val yaml =
      """
        |parType: class
        |value: model.effects.CopyEffect
        |parameters:
        |""".stripMargin
    val res: Either[String, Any] = parseParameter(yaml)
    assert(res.getOrElse(Gold(1)).isInstanceOf[CopyEffect])
