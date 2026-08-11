package controller.choices

import controller.dto.{DieDTO, PlayerDTO}
import model.ModelPublisher
import model.ModelPublisher.ModelContext.{DiceThrowEnd}
import model.ModelPublisher.ModelSubscriber
import model.Players.Player
import model.effects.EffectManager
import model.utils.RandomModules.given_RandomModule_Int

class DieChoiceAndRollController(player: Player) extends ChoiceController[DieDTO] with ModelSubscriber:
  this.setPublisher(ModelPublisher())
  private var pendingCount = player.pendingRolls - 1
  private var dieIndex: Int = 0

  private def rollDie(): Unit =
    EffectManager().updateTurnEffects(Seq((player, player.dice(dieIndex).roll, dieIndex)))
    EffectManager().attemptSolve(Seq((player, player.dice(dieIndex).lastRolledEffect.get)))

  override def pendingChoices: Seq[PlayerChoice[DieDTO]] = Seq((PlayerDTO(player), player.dice.map(DieDTO(_))))
  override def resumeAfterChoices(results: Seq[Int]): Unit =
    dieIndex = results.head
    rollDie()

  override def update(context: ModelPublisher.ModelContext): Unit = context match
    case DiceThrowEnd =>
      if pendingCount > 0
      then
        pendingCount = pendingCount - 1
        rollDie()
    case _ =>