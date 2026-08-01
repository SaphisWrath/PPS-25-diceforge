package model.utils

import model.GameMatch
import model.Players.Player
import model.effects.*
import model.resource.Gold
import model.utils.RandomModules.given_RandomModule_Int

trait TemporaryDie:
  def maxFaces: Int
  def roll(using randomModule: RandomModule[Int]): ResourceEffect
  def addFaces(addedFaces: ResourceEffect*): Unit

trait DiceThrow:
  def initiateDiceRoll(dice: Seq[(Player, Seq[TemporaryDie])]):
    (Seq[(Player, CopyEffect)], Seq[(Player, ResourceEffect)])
  def sortEffects(effects: Seq[(Player, ResourceEffect)]): Seq[(Player, OptionEffect)]
  def resolveAll(effects: Seq[(Player, ResourceEffect)]): Unit

object DiceThrow:
  private def beforeActivePlayer(gameMatch: GameMatch)(player: Player): Boolean =
    val players = gameMatch.players
    players.indexOf(player) < players.indexOf(gameMatch.activePlayer)

  private class DiceThrowImpl(gameMatch: GameMatch) extends DiceThrow:
    private val orderCheck = beforeActivePlayer(gameMatch)
    private var nonCopyEffects: Seq[(Player, ResourceEffect)] = Seq.empty
    private var nonOptionEffects: Seq[(Player, ResourceEffect)] = Seq.empty
    
    override def initiateDiceRoll(dice: Seq[(Player, Seq[TemporaryDie])]):
      (Seq[(Player, CopyEffect)], Seq[(Player, ResourceEffect)]) =
      val (copyEffects, resourceEffects) = dice
        .sortBy((p, _) => if orderCheck(p) then 1 else -1)
        .flatMap((p, d) => d.map(dice => (p, dice.roll)))
        .partition((_, e) => e.isInstanceOf[CopyEffect])
      nonCopyEffects = resourceEffects
      (copyEffects.map((p, e) => (p, e.asInstanceOf[CopyEffect])), resourceEffects)

    override def sortEffects(effects: Seq[(Player, ResourceEffect)]):
      Seq[(Player, OptionEffect)] =
      val (optionEffects, resourceEffects) = effects
        .concat(nonCopyEffects)
        .sortBy((p, _) => if orderCheck(p) then 1 else -1)
        .partition((_, e) => e.isInstanceOf[OptionEffect])
      nonOptionEffects = resourceEffects
      optionEffects.map((p, e) => (p, e.asInstanceOf[OptionEffect]))
    
    override def resolveAll(effects: Seq[(Player, ResourceEffect)]): Unit =
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
            case None => e.resource = Gold(0)
            case Some(resourceEffect) => e.resource = resourceEffect.resource
          e.resolve(correctPlayerDestination(p))
        )
      
  def apply(gameMatch: GameMatch): DiceThrow = DiceThrowImpl(gameMatch)