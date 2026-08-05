package model.missions

import model.Players.Player
import model.effects.{Effect, ResourceEffect, Target}
import model.resource.PlayerBoard

trait Mission:
  def reward: List[Effect]

  def cost: List[ResourceEffect]

  def id: String

  def get(receiverProducer: Target => Seq[Player]): Unit

  def canGet(receiverProducer: Target => Seq[Player]): Boolean

object Mission:
  def unapply(mission: Mission): (List[Effect], List[ResourceEffect], String) = (mission.reward, mission.cost, mission.id)

case class BaseMission(reward: List[Effect], cost: List[ResourceEffect], id: String = "placeholder") extends Mission:

  override def get(receiverProducer: Target => Seq[Player]): Unit =
    if canGet(receiverProducer) then
      cost.foreach(r => {
        r.setModule(model.utils.ResourceEffectModules.SubtractResource)
        r.resolve(receiverProducer(r.target))
      })
      obtainReward(receiverProducer)

  protected def obtainReward(receiverProducer: Target => Seq[Player]): Unit = {}

  override def canGet(receiverProducer: Target => Seq[Player]): Boolean = {
    cost.forall { e =>
      val players = receiverProducer(e.target)
      players.nonEmpty && players.forall(_.board.canSpend(e.resource))
    }
  }

trait LimitedPurchase(_startingPurchaseCount: Int) extends Mission:
  private var _purchaseCount = startingPurchaseCount
  private def availableForPurchase: Boolean = _purchaseCount > 0
  def purchaseCount: Int = _purchaseCount
  def startingPurchaseCount: Int = _startingPurchaseCount
  abstract override def get(receiverProducer: Target => Seq[Player]): Unit =
    if this.canGet(receiverProducer)
    then {
      super.get(receiverProducer)
      _purchaseCount = _purchaseCount - 1
    }

  abstract override def canGet(receiverProducer: Target => Seq[Player]): Boolean =
    availableForPurchase && super.canGet(receiverProducer)

object LimitedPurchaseMission:
  def unapply(mission: Mission): (List[Effect], List[ResourceEffect], String) = (mission.reward, mission.cost, mission.id)

trait InstantRewards extends BaseMission:
  override def obtainReward(receiverProducer: Target => Seq[Player]): Unit =
    super.obtainReward(receiverProducer)
    reward.foreach {
      case res@ResourceEffect(_, _, _) =>
        res.resolve(receiverProducer(res.target))
    }

trait SupportRewards(supportCost: List[ResourceEffect]) extends BaseMission:
  override def obtainReward(receiverProducer: Target => scala.Seq[Player]): Unit =
    super.obtainReward(receiverProducer)
    val player = receiverProducer(Target.Self).head
    player.addMission(ObtainedMission(reward, supportCost, player, id))

trait Obtained(owner: Player) extends BaseMission:
  override def get(receiverProducer: Target => scala.Seq[Player] = _ => Seq(owner)): Unit = super.get(receiverProducer)

class InstantMission(reward: List[Effect], cost: List[ResourceEffect], id: String = "placeholder", startCount: Int = 4)
  extends BaseMission(reward, cost, id) with InstantRewards with LimitedPurchase(startCount)

class SupportMission(reward: List[Effect], supportCost: List[ResourceEffect], missionCost: List[ResourceEffect], id: String = "placeholder", startCount: Int = 4)
  extends BaseMission(reward, missionCost, id) with SupportRewards(supportCost) with LimitedPurchase(startCount)

class ObtainedMission(rewards: List[Effect], cost: List[ResourceEffect], owner: Player, id: String = "placeholder")
  extends BaseMission(rewards, cost, id) with InstantRewards with Obtained(owner)