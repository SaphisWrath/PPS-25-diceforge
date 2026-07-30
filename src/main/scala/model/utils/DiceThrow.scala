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
  def resolveAll(gameMatch: GameMatch, dice: List[(Player, List[TemporaryDie])]): Unit

object DiceThrow:
  private def transformCopyEffect(availableEffects: List[CarriesResource]): CarriesResource =
    ResourceEffect(Gold(0)) //  TODO

  private def beforeActivePlayer(gameMatch: GameMatch)(player: Player): Boolean =
    val players = gameMatch.players
    players.indexOf(player) < players.indexOf(gameMatch.activePlayer)

  private class DiceThrowImpl extends DiceThrow:
    override def resolveAll(gameMatch: GameMatch, dice: List[(Player, List[TemporaryDie])]): Unit =
      val (copyEffects, standardEffects) = dice
        .flatMap((p, d) => d.map(dice => (p, dice.roll)))
        .partition((_, e) => e.isInstanceOf[CopyEffect])

      val orderCheck = beforeActivePlayer(gameMatch)
      val (resourceEffects, multiplyEffects) = copyEffects
        .map((p, _) => (p, transformCopyEffect(standardEffects.map(_._2))))
        .concat(standardEffects)
        .sortBy((p, _) => if orderCheck(p) then 1 else -1)
        .sortBy((_, e) => if e.isInstanceOf[OptionEffect] then 0 else 1)
        .partition((_, e) => !e.isInstanceOf[MultiplyEffect])

      resourceEffects.foreach(_._2.resolve())
      multiplyEffects
        .map((p, e) => (p, e.asInstanceOf[MultiplyEffect]))
        .foreach((p, e) =>
        resourceEffects
          .find((player, _) => player.getName == p.getName)
          .map(_._2) match
            case None => e.resource = Gold(0)
            case Some(resourceEffect) => e.resource = resourceEffect.getResource
        )