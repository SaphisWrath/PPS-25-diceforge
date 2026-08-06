package controller

import controller.dto.{CompoundEffectDTO, EffectDTO, PlayerDTO}
import model.Players.Player
import model.effects.Effect
import model.utils.EffectManager

object EffectSolveController:
  private def asPlayer(playerList: Seq[Player])(playerDTO: PlayerDTO): Player =
    playerList.find(p => p.name == playerDTO.name).get
    
  private def asEffect(effectsSource: Seq[Effect])(effectDTO: EffectDTO): Effect =
    def equalEffects(e1: EffectDTO, e2: EffectDTO): Boolean =
      def equalOptions[A](o1: Option[A], o2: Option[A]): Boolean =
        (o1.isEmpty && o2.isEmpty) || (o1.isDefined && o2.isDefined && o1.contains(o2.get))
      e1.sprite == e2.sprite && equalOptions(e1.label, e2.label)

    effectsSource.find(effect => (EffectDTO(effect), effectDTO) match
      case (CompoundEffectDTO(effects_1), CompoundEffectDTO(effects_2)) =>
        effects_1.zip(effects_2).forall((e1, e2) => equalEffects(e1, e2))
      case (e1: EffectDTO, e2: EffectDTO) => equalEffects(e1,e2)
    ).get

  private class EffectSolveControllerImpl extends ChoiceController[EffectDTO]:
    private val effectManager = EffectManager()
    private var effectList: Seq[(Player, Effect)] = Seq.empty

    override def pendingChoices: Seq[PlayerChoice[EffectDTO]] =
      effectList = effectManager.effectsToSolve.flatMap((p, opt) => opt.effects.map((p, _)))
      effectManager.effectsToSolve.map((p, opt) => (PlayerDTO(p), opt.effects.map(EffectDTO(_))))

    override def resumeAfterChoices(results: Seq[(PlayerDTO, EffectDTO)]): Unit =
      effectManager.attemptSolve(
        results.map((pDTO, eDTO) => (asPlayer(effectList.map(_._1))(pDTO), asEffect(effectList.map(_._2))(eDTO)))
      )

  def apply(): ChoiceController[EffectDTO] = EffectSolveControllerImpl()