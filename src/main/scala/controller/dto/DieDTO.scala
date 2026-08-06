package controller.dto

import model.dice.Die

trait DieDTO:
  def faces: Seq[EffectDTO]

object DieDTO:
  private case class DieDTOImpl(faces: Seq[EffectDTO]) extends DieDTO
  def apply(die: Die): DieDTO = DieDTOImpl(die.faces.map(EffectDTO(_)))