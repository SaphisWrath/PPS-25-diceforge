package model.utils

import model.resource.Gold
import org.scalatest.flatspec.AnyFlatSpec
import model.utils.parsers.YAMLParsers.{parse, given}

class YAMLParserTest extends AnyFlatSpec:
  "parse" should "parse a resource from correct syntax" in:
    val yaml ="""
      |rType: gold
      |amount: 1
      |""".stripMargin
    assert(parse(yaml) == Right(Gold(1)))

  "parse" should "parse an effect from correct syntax" in:
    val yaml =
      """
        |eType: "res"
        |
        |""".stripMargin
