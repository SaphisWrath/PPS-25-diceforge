package controller.dto

import controller.converters.ResourceConverters.stringToResourceBuilder
import controller.dto.pathfinders.ImagePathFinders.{findImagePath, given}
import model.effects.ThrowEffects.{ThrowAllDice, ThrowOneDie, ThrowTimesEffect}
import model.effects.{UpdateCapacityEffect, *}
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
      case e : CompoundEffect => CompoundEffectDTO(e.effects.map(EffectDTO(_)), e match
        case sum: SumEffect => Some("+")
        case opt: OptionEffect => Some("?")
        case _ => Option.empty
      )
      case ResourceEffect(resource, _, _) => EffectDTO(
        findImagePath(effect),
        Some(resource.amount.toString)
      )
      case UpdateCapacityEffect(resource) => EffectDTO(
        findImagePath(effect),
        Some("+" + resource.amount.toString)
      )
      case MultiplyEffect(multiplier) => EffectDTO (
        findImagePath(effect),
        Some(multiplier.toString)
      )
      case GrantFaceEffect(newFace) => EffectDTO(
        findImagePath(effect),
        Some("++")
      )
      case t: ThrowTimesEffect => EffectDTO(
        findImagePath(effect),
        Some(s"×${t.times.toString}")
      )
      case t: ThrowOneDie => EffectDTO(
        findImagePath(effect),
        Some(s"×${t.times.toString}")
      )
      case _ => EffectDTO(findImagePath(effect), Option.empty)
