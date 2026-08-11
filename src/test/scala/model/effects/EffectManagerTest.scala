package model.effects

import mock.MockPlayer
import model.ModelPublisher
import model.ModelPublisher.ModelContext.{EffectChoiceContext, ResourceContext}
import model.ModelPublisher.ModelSubscriber
import model.Players.Color.{Green, Orange}
import model.effects.Target.Self
import model.resource.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class EffectManagerTest extends AnyFlatSpec with Matchers:
  private val effectManager = EffectManager()
  private var choiceCalls = 0
  private var successfulSolves = 0
  private val choiceListener = new ModelSubscriber {
    this.setPublisher(ModelPublisher())
    override def update(context: ModelPublisher.ModelContext): Unit = context match
      case EffectChoiceContext => choiceCalls = choiceCalls + 1
      case ResourceContext => successfulSolves = successfulSolves + 1
      case _ =>
  }

  private def resetChoiceListenerRecord(): Unit =
    choiceCalls = 0
    successfulSolves = 0

  private def newPlayers = Seq(
    MockPlayer("Mario", Orange),
    MockPlayer("Luigi", Green)
  )

  "An EffectManager" should "solve ResourceEffects without outer help" in:
    resetChoiceListenerRecord()
    val players = newPlayers
    val goldAmount = 3
    val sunCrystalAmount = 2
    val effects = Seq(
      (players.head, ResourceEffect(Gold(goldAmount), Self)),
      (players.head, ResourceEffect(SunCrystal(sunCrystalAmount), Self)),
    )

    effectManager.attemptSolve(effects, true)
    choiceCalls should be(0)
    players.head.board.gold.amount should be(goldAmount)
    players.head.board.sunCrystals.amount should be(sunCrystalAmount)

  "An EffectManager" should "handle SumEffects and MultiplyEffects accordingly" in:
    resetChoiceListenerRecord()
    val players = newPlayers
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
    choiceCalls should be(0)
    players.head.board.gloryPoints.amount should be(gloryPointAmount * multiplier)
    players.head.board.moonCrystals.amount should be(moonCrystalAmount * multiplier)

  "An EffectManager" should "ask the player how to solve OptionEffects and CopyEffects" in:
    resetChoiceListenerRecord()
    val players = newPlayers
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
    choiceCalls should be(1)

    effectManager.attemptSolve(effectManager.effectsToSolve.map((p, e) => (p, e.effects.head)))
    effectManager.effectsToSolve should contain(effects.head)
    effectManager.effectsToSolve should contain((players(1), effects.head._2))
    choiceCalls should be(2)

    effectManager.attemptSolve(effectManager.effectsToSolve.map((p, e) => (p, e.effects.head)))
    players.head.board.gold.amount should be(goldAmount)
    players.head.board.sunCrystals.amount should be(0)
    players(1).board.gold.amount should be(goldAmount)
    players(1).board.sunCrystals.amount should be(0)
    choiceCalls should be(2)

  "An EffectManager" should "handle SumEffect chains accordingly" in:
    resetChoiceListenerRecord()
    val players = newPlayers
    val goldAmount = 3
    val effects = Seq(
      (players.head, SumEffect(Seq(SumEffect(Seq(ResourceEffect(Gold(goldAmount), Self))))))
    )

    effectManager.attemptSolve(effects, true)
    players.head.board.gold.amount should be(goldAmount)