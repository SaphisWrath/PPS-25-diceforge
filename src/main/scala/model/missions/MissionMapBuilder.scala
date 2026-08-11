package model.missions

import model.effects.*
import model.effects.Target.Self
import model.resource.{GloryPoint, Gold, MoonCrystal, SunCrystal}

object MissionMapBuilder:
  /**
   * @return a map of placeholder missions for testing
   */
  def makePlaceholderMissions: Map[Int, List[Mission]] =
    val cost: List[ResourceEffect] = List(ResourceEffect(Gold(3), Self))
    val reward: List[ResourceEffect] = List(ResourceEffect(SunCrystal(3), Self), ResourceEffect(MoonCrystal(3), Self))
    val victoryPoints: List[Effect] = List(ResourceEffect(GloryPoint(2), Self))
    val placeholderMission = SupportMission(victoryPoints, cost, reward, cost, "one", 2)
    val placeholderInstant = InstantMission(reward, cost, "two", 2)
    val placeholderGrantFace = InstantMission(reward.concat(Seq(MultiplyEffect(3))), cost, "three", 2)
    List(
      0 -> List(placeholderMission, placeholderInstant),
      1 -> List(placeholderMission, placeholderMission),
      2 -> List(placeholderMission, placeholderMission),
      3 -> List(placeholderInstant, placeholderMission),
      4 -> List(placeholderMission, placeholderMission),
      5 -> List(placeholderMission, placeholderMission),
      6 -> List(placeholderMission, placeholderInstant, placeholderGrantFace)
    ).toMap

  /**
   * @param startCount the amount of times a mission can be completed
   * @return the standard map of missions
   */
  def makeStandardMissions(startCount: Int): Map[Int, List[Mission]] = {
    val builder = MissionFactory(startCount)
    List(
      0 -> List(builder.makeSpirits, builder.makeElder),
      1 -> List(builder.makeOwl, builder.makeMinotaur),
      2 -> List(builder.makeGorgon, builder.makeMirror),
      3 -> List(builder.makeSmithHammer, builder.makeSmithChest),
      4 -> List(builder.makeSilverDoe, builder.makeSatyr),
      5 -> List(builder.makeFerryman, builder.makeHelmet),
      6 -> List(builder.makeSphinx, builder.makeScorpion, builder.makeHydra)
    ).toMap
  }
