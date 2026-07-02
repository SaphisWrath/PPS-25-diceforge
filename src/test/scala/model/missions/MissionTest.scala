package model.missions

import model.dice.Effect.{MultiplyEffect, ResourceEffect}
import model.resource.Gold
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.funsuite.AnyFunSuite

class MissionTestSuite extends AnyFunSuite {
  test("makeEmpty") {
    val mission = Mission.makeEmpty()
    assert(mission.cost == List.empty)
    assert(mission.reward == List.empty)
  }
}

class MissionTestSpec extends AnyFlatSpec{
  "A mission reward" should "contain the list of effects from construction" in:
    val rewards = List(
      ResourceEffect(Gold(10)),
      MultiplyEffect(2),
      MultiplyEffect(3)
    )
    val mission = Mission(rewards, List(ResourceEffect(Gold(1))))
    assert(mission.reward == rewards)
}
