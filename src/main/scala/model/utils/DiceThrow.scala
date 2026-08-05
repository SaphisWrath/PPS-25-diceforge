package model.utils

import model.GameMatch
import model.Players.Player
import model.dice.Die
import model.effects.*
import model.utils.RandomModules.given_RandomModule_Int

trait DiceThrow:
  def initiateDiceRoll(dice: Seq[(Player, Seq[Die])]):
    (Seq[(Player, CopyEffect)], Seq[(Player, Effect)])
  def sortEffects(effects: Seq[(Player, Effect)]): Seq[(Player, OptionEffect)]
  def resolveAll(effects: Seq[(Player, Effect)]): Unit

object DiceThrow:
  private def beforeActivePlayer(gameMatch: GameMatch)(player: Player): Boolean =
    val players = gameMatch.players
    players.indexOf(player) < players.indexOf(gameMatch.activePlayer)

  private class DiceThrowImpl(gameMatch: GameMatch) extends DiceThrow:
    private val orderCheck = beforeActivePlayer(gameMatch)
    private var nonCopyEffects: Seq[(Player, Effect)] = Seq.empty
    private var nonOptionEffects: Seq[(Player, Effect)] = Seq.empty
    
    override def initiateDiceRoll(dice: Seq[(Player, Seq[Die])]):
      (Seq[(Player, CopyEffect)], Seq[(Player, Effect)]) =
      val (copyEffects, resourceEffects) = dice
        .sortBy((p, _) => if orderCheck(p) then 1 else -1)
        .flatMap((p, d) => d.map(dice => (p, dice.roll)))
        .partition((_, e) => e.isInstanceOf[CopyEffect])
      nonCopyEffects = resourceEffects
      (copyEffects.map((p, e) => (p, e.asInstanceOf[CopyEffect])), resourceEffects)

    override def sortEffects(effects: Seq[(Player, Effect)]):
      Seq[(Player, OptionEffect)] =
      val (optionEffects, resourceEffects) = effects
        .concat(nonCopyEffects)
        .sortBy((p, _) => if orderCheck(p) then 1 else -1)
        .partition((_, e) => e.isInstanceOf[OptionEffect])
      nonOptionEffects = resourceEffects
      optionEffects.map((p, e) => (p, e.asInstanceOf[OptionEffect]))
    
    override def resolveAll(effects: Seq[(Player, Effect)]): Unit =
      val (multiplyEffects, resourceEffects) = effects
        .concat(nonOptionEffects)
        .sortBy((p, _) => if orderCheck(p) then 1 else -1)
        .partition((_, e) => e.isInstanceOf[MultiplyEffect])

      val correctPlayerDestination: Player => Player = p => gameMatch.players.find(player => player.name == p.name).get

      resourceEffects.foreach((p, e) => e.resolve(correctPlayerDestination(p)))
      multiplyEffects
        .map((p, e) => (p, e.asInstanceOf[MultiplyEffect]))
        .foreach((p, e) =>
          resourceEffects
            .find((player, _) => player.name == p.name)
            .map(_._2) match
            case Some(effect) => e.currentEffect = effect
            case _ =>
          e.resolve(correctPlayerDestination(p))
        )
      
  def apply(gameMatch: GameMatch): DiceThrow = DiceThrowImpl(gameMatch)