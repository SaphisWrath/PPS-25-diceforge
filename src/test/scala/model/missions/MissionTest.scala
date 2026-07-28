package model.missions

import model.Players.Color.Orange
import model.Players.Player
import model.effects.ResourceEffect
import model.resource.{Gold, MoonCrystal, PlayerBoard, SunCrystal}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.funsuite.AnyFunSuite

val costAmount = 3
val rewardAmount = 3
val cost: List[ResourceEffect] = (ResourceEffect(SunCrystal(costAmount)), ResourceEffect(MoonCrystal(costAmount))).toList
val reward: List[ResourceEffect] = List(ResourceEffect(Gold(rewardAmount)))
val player: Player = Player("Mario", Orange)

class MissionTestSuite extends AnyFunSuite:
  test("canGet"):
    val mission = BaseMission(reward, cost)
    val board = PlayerBoard.emptyBoard(player)
    assert(!mission.canGet(board))
    board.addResource(SunCrystal(costAmount))
    assert(!mission.canGet(board))

class MissionTestSpec extends AnyFlatSpec:
  val board = PlayerBoard(player, 0, 3, 3, 0)
  val cost: List[ResourceEffect] = (ResourceEffect(SunCrystal(3)), ResourceEffect(MoonCrystal(3))).toList
  val reward: List[ResourceEffect] = List(ResourceEffect(Gold(3)))

  "Any mission" should "take its cost from player resources when activated" in:
    val board = PlayerBoard(player, 0, costAmount, costAmount, 0)
    val mission = BaseMission(reward, cost)
    mission.get(board)
    assert(board.sunCrystals.amount == 0)
    assert(board.moonCrystals.amount == 0)

  "InstantMission" should "grant its reward immediately upon acquisition" in:
    val board = PlayerBoard(player, 0, costAmount, costAmount, 0)
    InstantMission(reward, cost).get(board)
    assert(board.gold.amount == rewardAmount)

  "InstantMission" should "both grant its reward and subtract its cost upon acquisition" in:
    val board = PlayerBoard(player, 0, costAmount, costAmount, 0)
    InstantMission(reward, cost).get(board)
    assert(board.sunCrystals.amount == 0)
    assert(board.moonCrystals.amount == 0)
    assert(board.gold.amount == rewardAmount)

  "Any Mission" should "not affect playerboard if player does not have necessary resources" in:
    val board = PlayerBoard(player, 0, costAmount, 0, 0)
    InstantMission(reward, cost).get(board)
    assert(board.sunCrystals.amount == costAmount)
    assert(board.moonCrystals.amount == 0)
    assert(board.gold.amount == 0)