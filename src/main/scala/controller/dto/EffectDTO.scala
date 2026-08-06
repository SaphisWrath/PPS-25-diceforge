package controller.dto

import controller.converters.ResourceConverters.stringToResourceBuilder
import controller.dto.pathfinders.ImagePathFinders.{findImagePath, given}
import model.effects.*
import model.resource.Resource
import view.LanguageStrings

case class EffectDTO(sprite: String, label: Option[String])
class CompoundEffectDTO(val effects: Seq[EffectDTO], label: Option[String]) extends EffectDTO(effects.head.sprite, label)

object CompoundEffectDTO:
  def unapply(compoundEffectDTO: CompoundEffectDTO): Option[Seq[EffectDTO]] = Some(compoundEffectDTO.effects)

object EffectDTO:
  private def extractResource(fromString: String): Resource =
    val strings = fromString.split(LanguageStrings.separator)
    stringToResourceBuilder(strings(0))(strings(1).toInt)

  def apply(effect: Effect): EffectDTO =
    effect match
      case CompoundEffect(effects) => CompoundEffectDTO(effects.map(EffectDTO(_)), Option.empty)
      case ResourceEffect(resource, _, _) => EffectDTO(
        findImagePath(resource),
        Some(resource.amount.toString)
      )
      case m @ MultiplyEffect(multiplier) => EffectDTO (
        findImagePath(effect),
        Some(m.multiplier.toString)
      )
      case _ => EffectDTO(findImagePath(effect), Option.empty)
