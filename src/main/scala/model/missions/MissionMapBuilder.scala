package model.missions

import model.effects.ResourceEffect
import model.resource.{Gold, MoonCrystal, SunCrystal}

object MissionMapBuilder:
  def makePlaceholderMissions: Map[Int, List[Mission]] =
    val cost: List[ResourceEffect] = List(ResourceEffect(Gold(3)))
    val reward: List[ResourceEffect] = List(ResourceEffect(SunCrystal(3)), ResourceEffect(MoonCrystal(3)), ResourceEffect(Gold(3)))
    val placeholderMission = InstantMission(reward, cost, "one")
    List(
      0 -> List(placeholderMission, placeholderMission),
      1 -> List(placeholderMission, placeholderMission),
      2 -> List(placeholderMission, placeholderMission),
      3 -> List(placeholderMission, placeholderMission),
      4 -> List(placeholderMission, placeholderMission),
      5 -> List(placeholderMission, placeholderMission),
      6 -> List(placeholderMission, placeholderMission, placeholderMission)
    ).toMap
