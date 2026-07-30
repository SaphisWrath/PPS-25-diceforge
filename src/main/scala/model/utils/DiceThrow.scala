package model.utils

import model.GameMatch
import model.Players.Player
import model.effects.{CarriesResource, CopyEffect, Effect, MultiplyEffect, OptionEffect, ResourceEffect}
import model.resource.Gold
import model.utils.RandomModules.given_RandomModule_Int

trait TemporaryDie:
  def maxFaces: Int
  def roll(using randomModule: RandomModule[Int]): CarriesResource
  def addFaces(addedFaces: CarriesResource*): Unit

trait DiceThrow:
  def initiateDiceRoll(dice: Seq[(Player, List[TemporaryDie])]):
    (Seq[(Player, CopyEffect)], Seq[(Player, CarriesResource)])
  def sortEffects(effects: Seq[(Player, CarriesResource)]):
    (Seq[(Player, OptionEffect)], Seq[(Player, CarriesResource)])
  def resolveAll(effects: Seq[(Player, CarriesResource)]): Unit

object DiceThrow:
  private def transformCopyEffect(availableEffects: List[CarriesResource]): CarriesResource =
    ResourceEffect(Gold(0)) //  TODO

  private def beforeActivePlayer(gameMatch: GameMatch)(player: Player): Boolean =
    val players = gameMatch.players
    players.indexOf(player) < players.indexOf(gameMatch.activePlayer)

  private class DiceThrowImpl(gameMatch: GameMatch) extends DiceThrow:
    override def initiateDiceRoll(dice: Seq[(Player, List[TemporaryDie])]):
      (Seq[(Player, CopyEffect)], Seq[(Player, CarriesResource)]) =
      val orderCheck = beforeActivePlayer(gameMatch)
      val (copyEffects, resourceEffects) = dice
        .sortBy((p, _) => if orderCheck(p) then 1 else -1)
        .flatMap((p, d) => d.map(dice => (p, dice.roll)))
        .partition((_, e) => e.isInstanceOf[CopyEffect])
      (copyEffects.map((p, e) => (p, e.asInstanceOf[CopyEffect])), resourceEffects)

    override def sortEffects(effects: Seq[(Player, CarriesResource)]):
      (Seq[(Player, OptionEffect)], Seq[(Player, CarriesResource)]) =
      val (optionEffects, resourceEffects) = effects
        .partition((_, e) => e.isInstanceOf[OptionEffect])
      (optionEffects.map((p, e) => (p, e.asInstanceOf[OptionEffect])), resourceEffects)
      
    override def resolveAll(effects: Seq[(Player, CarriesResource)]): Unit =
      val (multiplyEffects, resourceEffects) = effects
        .partition((_, e) => e.isInstanceOf[MultiplyEffect])
      
      resourceEffects.foreach(_._2.resolve())
      multiplyEffects
        .map((p, e) => (p, e.asInstanceOf[MultiplyEffect]))
        .foreach((p, e) =>
          resourceEffects
            .find((player, _) => player.getName == p.getName)
            .map(_._2) match
            case None => e.resource = Gold(0)
            case Some(resourceEffect) => e.resource = resourceEffect.getResource
          e.resolve()
        )