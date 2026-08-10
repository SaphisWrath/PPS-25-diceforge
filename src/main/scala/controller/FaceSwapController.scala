package controller

import controller.dto.{EffectDTO, PlayerDTO}
import model.Players.Player
import model.dice.Die

object FaceSwapController:
  private class FaceSwapControllerImpl(player: Player, die: Die) extends ChoiceController[EffectDTO]:
    override def pendingChoices: Seq[PlayerChoice[EffectDTO]] =
      Seq((PlayerDTO(player), die.faces.map(EffectDTO(_))))

    override def resumeAfterChoices(results: Seq[Int]): Unit =
      die.addFaceFromQueue(die.faces(results.head))

  def apply(player: Player, die: Die): ChoiceController[EffectDTO] =
    FaceSwapControllerImpl(player, die)