package controller.choices

import controller.dto.{EffectDTO, PlayerDTO}
import model.Players.Player
import model.effects.{Effect, EffectManager}

object EffectSolveController:
  private class EffectSolveControllerImpl extends ChoiceController[EffectDTO]:
    private val effectManager = EffectManager()
    private var choiceList: Seq[(Player, Seq[Effect])] = Seq.empty

    override def pendingChoices: Seq[PlayerChoice[EffectDTO]] =
      choiceList = effectManager.effectsToSolve.map((p, opt) => (p, opt.effects))
      choiceList.map((p, effects) => (PlayerDTO(p), effects.map(EffectDTO(_))))

    override def resumeAfterChoices(results: Seq[Int]): Unit =
      effectManager.attemptSolve(results.zip(choiceList).map((index, choice) => (choice._1, choice._2(index))))

  def apply(): ChoiceController[EffectDTO] = EffectSolveControllerImpl()