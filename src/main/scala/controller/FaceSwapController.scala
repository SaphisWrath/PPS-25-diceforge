package controller

import controller.dto.{EffectDTO, PlayerDTO}
import model.Players.Player
import model.dice.Die
import model.effects.Effect

object FaceSwapController:
  //  Main controller should construct this easily everytime it's requested
  //  player -> activePlayer, newFace -> shop last item
  private class FaceSwapControllerImpl(player: Player, die: Die, newFace: Effect) extends ChoiceController[EffectDTO]:
    override def pendingChoices: Seq[PlayerChoice[EffectDTO]] =
      Seq((PlayerDTO(player), die.faces.map(EffectDTO(_))))

    override def resumeAfterChoices(results: Seq[Int]): Unit =
      die.addFace(newFace, Option(die.faces(results.head)))

  def apply(player: Player, die: Die, newFace: Effect): ChoiceController[EffectDTO] =
    FaceSwapControllerImpl(player, die, newFace)