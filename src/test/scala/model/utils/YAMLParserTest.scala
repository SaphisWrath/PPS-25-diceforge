package model.utils

import model.resource.Gold
import org.scalatest.flatspec.AnyFlatSpec
import YAMLParsers.{parse, given}

class YAMLParserTest extends AnyFlatSpec:
  "parseYAML" should "parse a resource from correct syntax" in:
    val yaml ="""
      |rType: gold
      |amount: 1
      |""".stripMargin
    println(yaml)
    assert(parse(yaml) == Right(Gold(1)))

