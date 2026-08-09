package model.missions

import model.effects.*
import model.effects.Target.Self
import model.resource.{GloryPoint, Gold, MoonCrystal, SunCrystal}

object MissionMapBuilder:
  def makePlaceholderMissions: Map[Int, List[Mission]] =
    val cost: List[ResourceEffect] = List(ResourceEffect(Gold(3), Self))
    val reward: List[ResourceEffect] = List(ResourceEffect(SunCrystal(3), Self), ResourceEffect(MoonCrystal(3), Self))
    val victoryPoints: List[Effect] = List(ResourceEffect(GloryPoint(2), Self))
    val placeholderMission = SupportMission(victoryPoints, cost, reward, cost, "one", 2)
    val placeholderInstant = InstantMission(reward, cost, "two", 2)
    val placeholderGrantFace = GrantFaceMission(reward, cost, MultiplyEffect(3), "three", 2)
    List(
      0 -> List(placeholderMission, placeholderInstant),
      1 -> List(placeholderMission, placeholderMission),
      2 -> List(placeholderMission, placeholderMission),
      3 -> List(placeholderInstant, placeholderMission),
      4 -> List(placeholderMission, placeholderMission),
      5 -> List(placeholderMission, placeholderMission),
      6 -> List(placeholderMission, placeholderInstant, placeholderGrantFace)
    ).toMap
