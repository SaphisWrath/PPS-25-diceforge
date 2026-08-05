package model.missions

import mock.MockPlayer
import model.Players.Color.Orange
import model.Players.Player
import model.effects.{ResourceEffect, Target}
import model.resource.{Gold, MoonCrystal, PlayerBoard, SunCrystal}
import org.scalatest.flatspec.AnyFlatSpec

val costAmount = 3
val rewardAmount = 3
val cost: List[ResourceEffect] = (ResourceEffect(SunCrystal(costAmount), Target.Self), ResourceEffect(MoonCrystal(costAmount), Target.Self)).toList
val reward: List[ResourceEffect] = List(ResourceEffect(Gold(rewardAmount), Target.Self))
val player: MockPlayer = MockPlayer("Mario", Orange)
val selfTargetProducer: Target => Seq[Player] = _ => Seq(player)
val mission = BaseMission(reward, cost)

class MissionTest extends AnyFlatSpec:
  "A player" can "only get a mission if they have the required resources" in:
    player.board = PlayerBoard.emptyBoard
    assert(!mission.canGet(selfTargetProducer))
    player.board.addResource(SunCrystal(costAmount))
    assert(!mission.canGet(selfTargetProducer))

  private def enoughResourceBoard: PlayerBoard = PlayerBoard(0, costAmount, costAmount, 0)

  "Any mission" should "take its cost from player resources when activated" in :
    player.board = enoughResourceBoard
    mission.get(selfTargetProducer)
    assert(player.board.sunCrystals.amount == 0)
    assert(player.board.moonCrystals.amount == 0)

  "InstantMission" should "grant its reward immediately upon acquisition" in :
    player.board = enoughResourceBoard
    InstantMission(reward, cost).get(selfTargetProducer)
    assert(player.board.gold.amount == rewardAmount)

  "InstantMission" should "both grant its reward and subtract its cost upon acquisition" in :
    player.board = enoughResourceBoard
    InstantMission(reward, cost).get(selfTargetProducer)
    assert(player.board.sunCrystals.amount == 0)
    assert(player.board.moonCrystals.amount == 0)
    assert(player.board.gold.amount == rewardAmount)

  "Support Mission" should "be obtained by the player" in :
    player.resetMissions()
    player.board = enoughResourceBoard
    SupportMission(reward, cost, cost).get(selfTargetProducer)
    assert(player.missions.nonEmpty)

  "Obtained Mission" should "let the player collect them" in :
    player.resetMissions()
    SupportMission(reward, cost, List.empty).get(selfTargetProducer)
    player.board = enoughResourceBoard
    player.missions.foreach(_.get(selfTargetProducer))
    assert(player.board.sunCrystals.amount == 0)
    assert(player.board.moonCrystals.amount == 0)
    assert(player.board.gold.amount == rewardAmount)

  "Any Mission" should "not affect playerboard if player does not have necessary resources" in :
    player.board = PlayerBoard(0, costAmount, 0, 0)
    InstantMission(reward, cost).get(selfTargetProducer)
    assert(player.board.sunCrystals.amount == costAmount)
    assert(player.board.moonCrystals.amount == 0)
    assert(player.board.gold.amount == 0)

