package model.missions

import model.effects.Target.{Others, Self}
import model.effects.ThrowEffects.{ThrowAllDice, ThrowOneDie, ThrowSubtractEffect}
import model.effects.*
import model.resource.{GloryPoint, Gold, MoonCrystal, SunCrystal}

object MissionFactory:
  class MissionFactoryImpl(startCount: Int):
    def makeFerryman: Mission = new InstantMission(
      cost = List(ResourceEffect(MoonCrystal(4), Self)),
      reward = List(ResourceEffect(GloryPoint(12), Self)),
      id = "ferryman",
      startCount = startCount
    )

    def makeGorgon: Mission = new InstantMission(
      cost = List(ResourceEffect(SunCrystal(4), Self)),
      reward = List(ResourceEffect(GloryPoint(12), Self)),
      id = "gorgon",
      startCount = startCount
    )

    def makeHydra: Mission = new InstantMission(
      cost = List(ResourceEffect(MoonCrystal(5), Self), ResourceEffect(SunCrystal(5), Self)),
      reward = List(ResourceEffect(GloryPoint(26), Self)),
      id = "hydra",
      startCount = startCount
    )

    def makeSmithChest: Mission = new InstantMission(
      cost = List(ResourceEffect(MoonCrystal(1), Self)),
      reward = List(
        ResourceEffect(GloryPoint(2), Self),
        UpdateCapacityEffect(Gold(4)),
        UpdateCapacityEffect(SunCrystal(3)),
        UpdateCapacityEffect(MoonCrystal(3))
      ),
      id = "smith_chest",
      startCount = startCount
    )

    def makeSatyr: Mission = new CopyOtherEffectsMission(
      cost = List(ResourceEffect(MoonCrystal(3), Self)),
      reward = List(ResourceEffect(GloryPoint(6), Self)),
      id = "satyr",
      startCount = startCount
    )

    def makeHelmet: Mission = new InstantMission(
      cost = List(ResourceEffect(MoonCrystal(5), Self)),
      reward = List(
        ResourceEffect(GloryPoint(4), Self),
        GrantFaceEffect(MultiplyEffect(3))
      ),
      id = "helmet",
      startCount = startCount
    )

    def makeSpirits: Mission = new InstantMission(
      cost = List(ResourceEffect(SunCrystal(1), Self)),
      reward = List(
        ResourceEffect(GloryPoint(2), Self),
        ResourceEffect(Gold(3), Self),
        ResourceEffect(MoonCrystal(3), Self)
      ),
      id = "spirits",
      startCount = startCount
    )

    def makeMinotaur: Mission = new InstantMission(
      cost = List(ResourceEffect(SunCrystal(3), Self)),
      reward = List(
        ResourceEffect(GloryPoint(8), Self),
        ThrowSubtractEffect(1, Others)
      ),
      id = "minotaur",
      startCount = startCount
    )

    def makeScorpion: Mission = new InstantMission(
      cost = List(ResourceEffect(MoonCrystal(3), Self)),
      reward = List(
        ResourceEffect(GloryPoint(8), Self),
        ThrowAllDice(2)
      ),
      id = "scorpion",
      startCount = startCount
    )

    def makeMirror: Mission = new InstantMission(
      cost = List(ResourceEffect(SunCrystal(5), Self)),
      reward = List(
        ResourceEffect(GloryPoint(10), Self),
        GrantFaceEffect(CopyEffect())
      ),
      id = "mirror",
      startCount = startCount
    )

    def makeSphinx: Mission = new InstantMission(
      cost = List(ResourceEffect(SunCrystal(6), Self)),
      reward = List(
        ResourceEffect(GloryPoint(10), Self),
        ThrowOneDie(4)
      ),
      id = "sphinx",
      startCount = startCount
    )

    def makeSmithHammer: Mission = new SupportMission(
      missionCost = List(ResourceEffect(MoonCrystal(1), Self)),
      missionReward = List.empty,
      supportCost = List(ResourceEffect(Gold(12), Self)),
      supportReward = List(ResourceEffect(GloryPoint(20), Self)),
      id = "smith_hammer",
      startCount = startCount
    )

    def makeSilverDoe: Mission = new SupportMission(
      missionCost = List(ResourceEffect(MoonCrystal(2), Self)),
      missionReward = List(ResourceEffect(GloryPoint(2), Self)),
      supportCost = List.empty,
      supportReward = List(ThrowOneDie(1)),
      id = "silver_doe",
      startCount = startCount
    )

    def makeElder: Mission = new SupportMission(
      missionCost = List(ResourceEffect(SunCrystal(1), Self)),
      missionReward = List.empty,
      supportCost = List(ResourceEffect(Gold(3), Self)),
      supportReward = List(ResourceEffect(GloryPoint(4), Self)),
      id = "elder",
      startCount = startCount
    )

    def makeOwl: Mission = new SupportMission(
      missionCost = List(ResourceEffect(SunCrystal(2), Self)),
      missionReward = List.empty,
      supportCost = List(ResourceEffect(Gold(3), Self)),
      supportReward = List(OptionEffect(Seq(
        ResourceEffect(Gold(1), Self),
        ResourceEffect(SunCrystal(1), Self),
        ResourceEffect(MoonCrystal(1), Self)
      ))),
      id = "owl",
      startCount = startCount
    )

  def apply(startCount: Int): MissionFactoryImpl = MissionFactoryImpl(startCount)