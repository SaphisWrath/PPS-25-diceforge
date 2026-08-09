package model.utils.parsers

import model.effects.Effect
import model.resource.*
import model.utils.parsers.Props.{ParameterProp, getParameter}
import org.virtuslab.yaml.{StringOps, YamlError}

import java.lang.reflect.Parameter

object YAMLParsers:
  def parseParameter(text: String): Either[String, Any] =
    val parameter: Either[YamlError, ParameterProp] = text.as[ParameterProp]
    parameter match
      case Left(error) => Left(error.msg)
      case Right(value) => getParameter(value)