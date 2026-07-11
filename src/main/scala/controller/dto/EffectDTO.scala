package controller.dto

import controller.converters.ResourceConverters.{GloryPointString, GoldString, MoonCrystalString, SunCrystalString, resourceToString, resourceWithAmountToString, separator, stringToResourceBuilder}
import model.effects.{Effect, ResourceEffect}
import model.resource.{GloryPoint, Gold, Resource}

case class EffectDTO(effectType: String, resource: Option[String])

object EffectDTO:
  private def extractResource(fromString: String): Resource = {
    val strings = fromString.split(separator)
    stringToResourceBuilder(strings(0))(strings(1).toInt)
  }

  def apply(effect: Effect): EffectDTO =
    effect match
      case ResourceEffect(resource, _) => EffectDTO(
        "+",
        Option(resourceWithAmountToString(resource))
      )
      case _ => EffectDTO("Unknown", Option.empty)
      
  def unapply(effectDTO: EffectDTO): Effect =
    effectDTO.effectType match
      case "+" => effectDTO.resource match
        case Some(s) => ResourceEffect(extractResource(s), None)
        case _ => ???