package controller

import controller.dto.{DieDTO, PlayerDTO}
import model.ModelPublisher
import model.ModelPublisher.ModelContext.ResourceContext
import model.ModelPublisher.ModelSubscriber
import model.Players.Player
import model.effects.EffectManager
import model.utils.RandomModules.given_RandomModule_Int

class DieChoiceAndRollController(player: Player) extends ChoiceController[DieDTO] with ModelSubscriber:
  var dieIndex: Int = 0
  private var rollCount: Int = player.pendingRolls

  private def rollDie(): Unit = EffectManager().attemptSolve(Seq((player, player.dice(dieIndex).roll)))
  override def pendingChoices: Seq[PlayerChoice[DieDTO]] = Seq((PlayerDTO(player), player.dice.map(DieDTO(_))))
  override def resumeAfterChoices(results: Seq[Int]): Unit =
    dieIndex = results.head
    rollCount = rollCount - 1
    rollDie()

  override def update(context: ModelPublisher.ModelContext): Unit = context match
    case ResourceContext =>
      if rollCount > 0
      then
        rollCount = rollCount - 1
        rollDie()
    case _ =>