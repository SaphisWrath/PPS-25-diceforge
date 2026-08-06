package controller.dto

import model.dice.Die

trait DieDTO:
  def faces: Seq[EffectDTO]
  def index: Int

object DieDTO:
  private case class DieDTOImpl(faces: Seq[EffectDTO], index: Int) extends DieDTO
  def apply(die: Die, index: Int): DieDTO = DieDTOImpl(die.faces.map(EffectDTO(_)), index)