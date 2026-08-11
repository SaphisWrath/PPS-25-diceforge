package model.effects

import mock.MockPlayer
import model.ModelPublisher
import model.ModelPublisher.ModelContext.{EffectChoiceContext, ResourceContext}
import model.ModelPublisher.ModelSubscriber
import model.Players.Color.{Green, Orange}
import model.effects.Target.Self
import model.resource.{Gold, SunCrystal}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class EffectManagerTest extends AnyFlatSpec with Matchers:
  private val effectManager = EffectManager()
  private var choiceCalls = 0
  private var successfulSolves = 0
  private val choiceListener = new ModelSubscriber {
    override def update(context: ModelPublisher.ModelContext): Unit = context match
      case EffectChoiceContext => choiceCalls = choiceCalls + 1
      case ResourceContext => successfulSolves = successfulSolves + 1
      case _ =>
  }

  private def resetChoiceListenerRecord(): Unit =
    choiceCalls = 0
    successfulSolves = 0

  private val players = Seq(
    MockPlayer("Mario", Orange),
    MockPlayer("Luigi", Green)
  )

  "An EffectManager" should "solve ResourceEffects without outer help" in:
    val goldAmount = 3
    val sunCrystalAmount = 2
    val effects = Seq(
      (players.head, ResourceEffect(Gold(goldAmount), Self)),
      (players.head, ResourceEffect(SunCrystal(sunCrystalAmount), Self)),
    )

    effectManager.attemptSolve(effects)
    choiceCalls should be(0)
    players.head.board.gold.amount should be(goldAmount)
    players.head.board.sunCrystals.amount should be(sunCrystalAmount)

