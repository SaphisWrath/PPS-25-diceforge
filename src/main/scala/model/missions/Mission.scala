package model.missions

import model.ModelPublisher
import model.ModelPublisher.ModelContext.{FaceObtainedContext, MissionContext, ResourceContext}
import model.Players.Player
import model.effects.Target.*
import model.effects.*
import model.resource.PlayerBoard
import model.utils.RandomModules.given_RandomModule_Int

trait Mission:
  def reward: List[Effect]

  def cost: List[ResourceEffect]

  def id: String

  def canGet(receiverProducer: Target => Seq[Player]): Boolean =
    cost.forall { e =>
      val players = receiverProducer(e.target)
      players.nonEmpty && players.forall(_.board.canSpend(e.resource))
    }

  final def get(receiverProducer: Target => Seq[Player]): Unit =
    if canGet(receiverProducer) then
      payCost(receiverProducer)
      applyEffects(receiverProducer)

  protected def payCost(receiverProducer: Target => Seq[Player]): Unit =
    cost.foreach(r => {
      r.setModule(model.utils.ResourceEffectModules.SubtractResource)
      r.resolve(receiverProducer(r.target))
    })

  protected def applyEffects(receiverProducer: Target => Seq[Player]): Unit

object Mission:
  def unapply(mission: Mission): (List[Effect], List[ResourceEffect], String) = (mission.reward, mission.cost, mission.id)

case class BaseMission(reward: List[Effect], cost: List[ResourceEffect], id: String = "placeholder") extends Mission:
  override protected def applyEffects(receiverProducer: Target => Seq[Player]): Unit = {}

trait Notification extends Mission:
  abstract override def applyEffects(receiverProducer: Target => Seq[Player]): Unit =
    super.applyEffects(receiverProducer)
    ModelPublisher().notify(ResourceContext)
    ModelPublisher().notify(MissionContext)

trait LimitedPurchase(_startingPurchaseCount: Int) extends Mission:
  private var _purchaseCount = startingPurchaseCount
  private def availableForPurchase: Boolean = _purchaseCount > 0
  def purchaseCount: Int = _purchaseCount
  def startingPurchaseCount: Int = _startingPurchaseCount
  abstract override def applyEffects(receiverProducer: Target => Seq[Player]): Unit =
    super.applyEffects(receiverProducer)
    _purchaseCount = _purchaseCount - 1

  abstract override def canGet(receiverProducer: Target => Seq[Player]): Boolean =
    availableForPurchase && super.canGet(receiverProducer)

trait InstantRewards extends Mission:
  abstract override def applyEffects(receiverProducer: Target => Seq[Player]): Unit =
    super.applyEffects(receiverProducer)
    reward.foreach(r => r.resolve(receiverProducer(r.target)))

trait SupportRewards(supportReward: List[Effect], supportCost: List[ResourceEffect]) extends Mission:
  abstract override def applyEffects(receiverProducer: Target => scala.Seq[Player]): Unit =
    super.applyEffects(receiverProducer)
    val player = receiverProducer(Self).head
    player.addMission(ObtainedMission(supportReward, supportCost, player, id))

trait Obtained extends Mission:
  private var _obtained: Boolean = false
  private def isObtained: Boolean = _obtained
  abstract override def canGet(receiverProducer: Target => Seq[Player]): Boolean =
    !isObtained && super.canGet(receiverProducer)
  abstract override def applyEffects(receiverProducer: Target => Seq[Player]): Unit =
    super.applyEffects(receiverProducer)
    _obtained = true
  def reset(): Unit = _obtained = false

class InstantMission(reward: List[Effect], cost: List[ResourceEffect], id: String = "placeholder", startCount: Int = 4)
  extends BaseMission(reward, cost, id) with InstantRewards with LimitedPurchase(startCount) with Notification

class SupportMission(
                      missionReward: List[Effect],
                      missionCost: List[ResourceEffect],
                      supportReward: List[Effect],
                      supportCost: List[ResourceEffect],
                      id: String = "placeholder",
                      startCount: Int = 4
                    )
  extends BaseMission(missionReward, missionCost, id)
    with InstantRewards with SupportRewards(supportReward, supportCost) with LimitedPurchase(startCount) with Notification

class ObtainedMission(rewards: List[Effect], cost: List[ResourceEffect], owner: Player, id: String = "placeholder")
  extends BaseMission(rewards, cost, id) with InstantRewards with Obtained with Notification

class CopyOtherEffectsMission(reward: List[Effect],
          cost: List[ResourceEffect],
          id: String = "placeholder",
          startCount: Int = 4) extends InstantMission(reward, cost, id, startCount):
  override def applyEffects(receiverProducer: Target => Seq[Player]): Unit = {
    super.applyEffects(receiverProducer)
    EffectManager().updateTurnEffects(
      receiverProducer(Others).flatMap(p => p.dice.zipWithIndex.map((d, i) => (p, d.roll, i)))
    )
    EffectManager().attemptSolve(
      LazyList
        .continually((receiverProducer(Self).head, CopyEffect()))
        .take(2)
    )
  }