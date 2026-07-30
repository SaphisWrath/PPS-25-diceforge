package controller.dto

import controller.converters.ResourceConverters.{resourceWithAmountToString, stringToResourceBuilder}
import model.effects.{Effect, ResourceEffect}
import model.resource.{GloryPoint, Gold, Resource}
import view.LanguageStrings

case class EffectDTO(effectType: String, resource: Option[String]):
  override def toString: String =
    effectType + resource.get

object EffectDTO:
  private def extractResource(fromString: String): Resource = {
    val strings = fromString.split(LanguageStrings.separator)
    stringToResourceBuilder(strings(0))(strings(1).toInt)
  }

  def apply(effect: Effect): EffectDTO =
    effect match
      case ResourceEffect(resource, _, _) => EffectDTO(
        "+",
        Option(resourceWithAmountToString(resource))
      )
      case _ => EffectDTO("Unknown", Option.empty)

  extension(effectDTO: EffectDTO)
    def toEffect: Effect =
      effectDTO.effectType match
        case "+" => effectDTO.resource match
          case Some(s) => ResourceEffect(extractResource(s), None)
          case _ => ???
