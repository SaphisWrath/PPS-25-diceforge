package model.missions

import model.effects.ResourceEffect
import model.resource.{Gold, MoonCrystal, PlayerBoard, SunCrystal}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.funsuite.AnyFunSuite

class MissionTestSuite extends AnyFunSuite {
  test("unapply") {

  }
}

class MissionTestSpec extends AnyFlatSpec{
  val board = PlayerBoard(0, 3, 3, 0)
  val cost: List[ResourceEffect] = (ResourceEffect(SunCrystal(3)), ResourceEffect(MoonCrystal(3))).toList
  val reward: List[ResourceEffect] = List(ResourceEffect(Gold(3)))

  "Any mission" should "take its cost from player resources when activated" in:
    val mission = BaseMission(reward, cost)
    mission.get(board)
    assert(board.sunCrystals.amount == 0)
    assert(board.moonCrystals.amount == 0)

  "InstantMission" should "grant its reward immediately upon aquisition" in:
    InstantMission(reward, cost).get(board)
    assert(board.gold.amount == 3)
}
