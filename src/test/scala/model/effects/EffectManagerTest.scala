package model.effects

import mock.MockPlayer
import model.Players.Color.{Green, Orange}
import model.effects.Target.Self
import model.resource.*
import model.utils.ResourceEffectModules
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class EffectManagerTest extends AnyFlatSpec with Matchers:
  private val effectManager = EffectManager(true)

  private def getPlayers = Seq(
    MockPlayer("Mario", Orange),
    MockPlayer("Luigi", Green)
  )

  "An EffectManager" should "solve ResourceEffects without outer help" in:
    val players = getPlayers
    val goldAmount = 3
    val sunCrystalAmount = 2
    val effects = Seq(
      (players.head, ResourceEffect(Gold(goldAmount), Self)),
      (players.head, ResourceEffect(SunCrystal(sunCrystalAmount), Self)),
    )

    effectManager.attemptSolve(effects, true)
    effectManager.effectsToSolve should be(Seq.empty)
    players.head.board.gold.amount should be(goldAmount)
    players.head.board.sunCrystals.amount should be(sunCrystalAmount)

  "An EffectManager" should "handle SumEffects and MultiplyEffects accordingly" in:
    val players = getPlayers
    val gloryPointAmount = 3
    val moonCrystalAmount = 2
    val multiplier = 3
    val effects = Seq(
      (players.head, SumEffect(Seq(
        ResourceEffect(GloryPoint(gloryPointAmount), Self),
        ResourceEffect(MoonCrystal(moonCrystalAmount), Self),
      ))),
      (players.head, MultiplyEffect(multiplier)),
    )

    effectManager.attemptSolve(effects, true)
    effectManager.effectsToSolve should be(Seq.empty)
    players.head.board.gloryPoints.amount should be(gloryPointAmount * multiplier)
    players.head.board.moonCrystals.amount should be(moonCrystalAmount * multiplier)

  "An EffectManager" should "ask the player how to solve OptionEffects and CopyEffects" in:
    val players = getPlayers
    val goldAmount = 3
    val sunCrystalAmount = 2
    val effects = Seq(
      (players.head, OptionEffect(Seq(
        ResourceEffect(Gold(goldAmount), Self),
        ResourceEffect(SunCrystal(sunCrystalAmount), Self)
      ))),
      (players(1), CopyEffect())
    )

    effectManager.attemptSolve(effects, true)
    effectManager.effectsToSolve.head should be((players(1), OptionEffect(Seq(effects.head._2))))

    effectManager.attemptSolve(effectManager.effectsToSolve.map((p, e) => (p, e.effects.head)))
    effectManager.effectsToSolve should contain(effects.head)
    effectManager.effectsToSolve should contain((players(1), effects.head._2))

    effectManager.attemptSolve(effectManager.effectsToSolve.map((p, e) => (p, e.effects.head)))
    effectManager.effectsToSolve should be(Seq.empty)
    players.head.board.gold.amount should be(goldAmount)
    players.head.board.sunCrystals.amount should be(0)
    players(1).board.gold.amount should be(goldAmount)
    players(1).board.sunCrystals.amount should be(0)

  "An EffectManager" should "handle SumEffect chains accordingly" in:
    val players = getPlayers
    val goldAmount = 3
    val effects = Seq(
      (players.head, SumEffect(Seq(SumEffect(Seq(ResourceEffect(Gold(goldAmount), Self))))))
    )

    effectManager.attemptSolve(effects, true)
    players.head.board.gold.amount should be(goldAmount)

  "Setting a module for a solve" should "only last for the first complete solve to follow" in:
    val players = getPlayers
    val goldAmount = 3
    val effects = Seq(
      (players.head, ResourceEffect(Gold(goldAmount), Self))
    )

    effectManager.attemptSolve(effects, true)
    players.head.board.gold.amount should be(goldAmount)
    effectManager.setModuleOnce(ResourceEffectModules.SubtractResource)
    effectManager.attemptSolve(effects)
    players.head.board.gold.amount should be(0)
    effectManager.attemptSolve(effects)
    players.head.board.gold.amount should be(goldAmount)