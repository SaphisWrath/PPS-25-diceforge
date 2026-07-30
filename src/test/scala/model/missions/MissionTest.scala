package model.missions

import mock.MockPlayer
import model.Players.Color.Orange
import model.Players.{Color, Player}
import model.effects.{ResourceEffect, Target}
import model.resource.{Gold, MoonCrystal, PlayerBoard, SunCrystal}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.funsuite.AnyFunSuite

val costAmount = 3
val rewardAmount = 3
val cost: List[ResourceEffect] = (ResourceEffect(SunCrystal(costAmount), Target.Self), ResourceEffect(MoonCrystal(costAmount), Target.Self)).toList
val reward: List[ResourceEffect] = List(ResourceEffect(Gold(rewardAmount), Target.Self))
val player: MockPlayer = MockPlayer("Mario", Orange)

class MissionTestSuite extends AnyFunSuite:
  test("canGet"):
    val mission = BaseMission(reward, cost)
    val selfTargetProducer: Target => Seq[Player] = _ => Seq(player)
    assert(!mission.canGet(selfTargetProducer))
    player.board.addResource(SunCrystal(costAmount))
    assert(!mission.canGet(selfTargetProducer))

class MissionTestSpec extends AnyFlatSpec:
  val selfTargetProducer: Target => Seq[Player] = _ => Seq(player)
  val cost: List[ResourceEffect] = (ResourceEffect(SunCrystal(3), Target.Self), ResourceEffect(MoonCrystal(3), Target.Self)).toList
  val reward: List[ResourceEffect] = List(ResourceEffect(Gold(3), Target.Self))

  "Any mission" should "take its cost from player resources when activated" in:
    player.board = PlayerBoard(0, costAmount, costAmount, 0)
    val mission = BaseMission(reward, cost)
    mission.get(selfTargetProducer)
    assert(player.board.sunCrystals.amount == 0)
    assert(player.board.moonCrystals.amount == 0)

  "InstantMission" should "grant its reward immediately upon acquisition" in:
    player.board = PlayerBoard(0, costAmount, costAmount, 0)
    InstantMission(reward, cost).get(selfTargetProducer)
    assert(player.board.gold.amount == rewardAmount)

  "InstantMission" should "both grant its reward and subtract its cost upon acquisition" in:
    player.board = PlayerBoard(0, costAmount, costAmount, 0)
    InstantMission(reward, cost).get(selfTargetProducer)
    assert(player.board.sunCrystals.amount == 0)
    assert(player.board.moonCrystals.amount == 0)
    assert(player.board.gold.amount == rewardAmount)

  "Support Mission" should "be obtained by the player" in:
    player.resetMissions()
    player.board = PlayerBoard(0, costAmount, costAmount, 0)
    SupportMission(reward, cost, cost).get(selfTargetProducer)
    assert(player.missions.nonEmpty)

  "Obtained Mission" should "let the player collect them" in:
    player.resetMissions()
    SupportMission(reward, cost, List.empty).get(selfTargetProducer)
    player.board = PlayerBoard(0, costAmount, costAmount, 0)
    player.missions.foreach(_.get())
    assert(player.board.sunCrystals.amount == 0)
    assert(player.board.moonCrystals.amount == 0)
    assert(player.board.gold.amount == rewardAmount)

  "Any Mission" should "not affect playerboard if player does not have necessary resources" in:
    player.board = PlayerBoard(0, costAmount, 0, 0)
    InstantMission(reward, cost).get(selfTargetProducer)
    assert(player.board.sunCrystals.amount == costAmount)
    assert(player.board.moonCrystals.amount == 0)
    assert(player.board.gold.amount == 0)

