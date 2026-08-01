package model.utils

import model.GameMatch
import model.Players.Player
import model.effects.*
import model.resource.Gold
import model.utils.RandomModules.given_RandomModule_Int

trait TemporaryDie:
  def maxFaces: Int
  def roll(using randomModule: RandomModule[Int]): CarriesResource
  def addFaces(addedFaces: CarriesResource*): Unit

trait DiceThrow:
  def initiateDiceRoll(dice: Seq[(Player, Seq[TemporaryDie])]):
    (Seq[(Player, CopyEffect)], Seq[(Player, CarriesResource)])
  def sortEffects(effects: Seq[(Player, CarriesResource)]): Seq[(Player, OptionEffect)]
  def resolveAll(effects: Seq[(Player, CarriesResource)]): Unit

object DiceThrow:
  private def beforeActivePlayer(gameMatch: GameMatch)(player: Player): Boolean =
    val players = gameMatch.players
    players.indexOf(player) < players.indexOf(gameMatch.activePlayer)

  private class DiceThrowImpl(gameMatch: GameMatch) extends DiceThrow:
    private val orderCheck = beforeActivePlayer(gameMatch)
    private var nonCopyEffects: Seq[(Player, CarriesResource)] = Seq.empty
    private var nonOptionEffects: Seq[(Player, CarriesResource)] = Seq.empty
    
    override def initiateDiceRoll(dice: Seq[(Player, Seq[TemporaryDie])]):
      (Seq[(Player, CopyEffect)], Seq[(Player, CarriesResource)]) =
      val (copyEffects, resourceEffects) = dice
        .sortBy((p, _) => if orderCheck(p) then 1 else -1)
        .flatMap((p, d) => d.map(dice => (p, dice.roll)))
        .partition((_, e) => e.isInstanceOf[CopyEffect])
      nonCopyEffects = resourceEffects
      (copyEffects.map((p, e) => (p, e.asInstanceOf[CopyEffect])), resourceEffects)

    override def sortEffects(effects: Seq[(Player, CarriesResource)]):
      Seq[(Player, OptionEffect)] =
      val (optionEffects, resourceEffects) = effects
        .concat(nonCopyEffects)
        .sortBy((p, _) => if orderCheck(p) then 1 else -1)
        .partition((_, e) => e.isInstanceOf[OptionEffect])
      nonOptionEffects = resourceEffects
      optionEffects.map((p, e) => (p, e.asInstanceOf[OptionEffect]))
    
    override def resolveAll(effects: Seq[(Player, CarriesResource)]): Unit =
      val (multiplyEffects, resourceEffects) = effects
        .concat(nonOptionEffects)
        .sortBy((p, _) => if orderCheck(p) then 1 else -1)
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
      
  def apply(gameMatch: GameMatch): DiceThrow = DiceThrowImpl(gameMatch)