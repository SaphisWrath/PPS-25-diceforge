package controller.dto

import model.dice.Face
import model.dice.Face.{OptionFace, SumFace}

case class FaceDTO(effects: List[EffectDTO], choose: Boolean)

object FaceDTO:
  def apply(face: Face): FaceDTO =
    face match
      case SumFace(effects) => FaceDTO(effects.map(e => EffectDTO(e)), false)
      case OptionFace(effects) => FaceDTO(effects.map(e => EffectDTO(e)), true)
      
  def toFace(faceDTO: FaceDTO): Face = {
    val effects = faceDTO.effects.map(e => EffectDTO.toEffect(e))
    if faceDTO.choose then OptionFace(effects) else SumFace(effects)
  }
