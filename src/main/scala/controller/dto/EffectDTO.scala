package controller.dto

import controller.converters.ResourceConverters.stringToResourceBuilder
import controller.dto.pathfinders.ImagePathFinders.{findImagePath, given}
import model.effects.{Effect, ResourceEffect}
import model.resource.Resource
import view.LanguageStrings

case class EffectDTO(sprite: String, label: Option[String])

object EffectDTO:
  private def extractResource(fromString: String): Resource = {
    val strings = fromString.split(LanguageStrings.separator)
    stringToResourceBuilder(strings(0))(strings(1).toInt)
  }

  def apply(effect: Effect): EffectDTO =
    effect match
      case ResourceEffect(resource, _) => EffectDTO(
        findImagePath(resource),
        Some(resource.amount.toString)
      )
      case _ => EffectDTO("Unknown", Option.empty)
