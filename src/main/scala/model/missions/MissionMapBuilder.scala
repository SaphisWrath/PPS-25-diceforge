package model.missions

import model.effects.ResourceEffect
import model.effects.Target.Self
import model.resource.{Gold, MoonCrystal, SunCrystal}

object MissionMapBuilder:
  def makePlaceholderMissions: Map[Int, List[Mission]] =
    val cost: List[ResourceEffect] = List(ResourceEffect(Gold(3), Self))
    val reward: List[ResourceEffect] = List(ResourceEffect(SunCrystal(3), Self), ResourceEffect(MoonCrystal(3), Self))
    val placeholderMission = SupportMission(reward, cost, cost, "one")
    List(
      0 -> List(placeholderMission, placeholderMission),
      1 -> List(placeholderMission, placeholderMission),
      2 -> List(placeholderMission, placeholderMission),
      3 -> List(placeholderMission, placeholderMission),
      4 -> List(placeholderMission, placeholderMission),
      5 -> List(placeholderMission, placeholderMission),
      6 -> List(placeholderMission, placeholderMission, placeholderMission)
    ).toMap
