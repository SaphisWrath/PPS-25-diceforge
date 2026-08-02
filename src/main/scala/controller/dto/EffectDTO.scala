package controller.dto

import controller.converters.ResourceConverters.stringToResourceBuilder
import controller.dto.pathfinders.ImagePathFinders.{findImagePath, given}
import model.effects.{Effect, MultiplyEffect, OptionEffect, ResourceEffect}
import model.resource.Resource
import view.LanguageStrings

case class EffectDTO(sprite: String, label: Option[String])

object EffectDTO:
  private def extractResource(fromString: String): Resource = {
    val strings = fromString.split(LanguageStrings.separator)
    stringToResourceBuilder(strings(0))(strings(1).toInt)
  }

  def apply(effect: OptionEffect): Seq[EffectDTO] =
    effect.options.map(EffectDTO(_))

  def apply(effect: Effect): EffectDTO =
    effect match
      case ResourceEffect(resource, _, _) => EffectDTO(
        findImagePath(resource),
        Some(resource.amount.toString)
      )
      case m @ MultiplyEffect(multiplier) => EffectDTO (
        findImagePath(effect),
        Some(m.multiplier.toString)
      )
      case _ => EffectDTO(findImagePath(effect), Option.empty)
