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

  def availableForPurchase: Boolean = purchaseCount > 0

  def purchaseCount: Int

  def startingPurchaseCount: Int

object Mission:
  def unapply(mission: Mission): (List[Effect], List[ResourceEffect], String) = (mission.reward, mission.cost, mission.id)

case class BaseMission(reward: List[Effect], cost: List[ResourceEffect], id: String = "placeholder", startingPurchaseCount: Int = 0) extends Mission:
  var purchaseCount: Int = startingPurchaseCount

  override def get(receiverProducer: Target => Seq[Player]): Unit =
    if canGet(receiverProducer) then
      purchaseCount = purchaseCount - 1
      cost.foreach(r => {
        r.setModule(model.utils.ResourceEffectModules.SubtractResource)
        r.resolve(receiverProducer(r.target))
      })
      obtainReward(receiverProducer)

  protected def obtainReward(receiverProducer: Target => Seq[Player]): Unit = {}

  override def canGet(receiverProducer: Target => Seq[Player]): Boolean = {
    availableForPurchase &&
    cost.forall { e =>
      val players = receiverProducer(e.target)
      players.nonEmpty && players.forall(_.board.canSpend(e.resource))
    }
  }

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
  extends BaseMission(reward, cost, id, startCount) with InstantRewards

class SupportMission(reward: List[Effect], supportCost: List[ResourceEffect], missionCost: List[ResourceEffect], id: String = "placeholder", startCount: Int = 4)
  extends BaseMission(reward, missionCost, id, startCount) with SupportRewards(supportCost)

class ObtainedMission(rewards: List[Effect], cost: List[ResourceEffect], owner: Player, id: String = "placeholder")
  extends BaseMission(rewards, cost, id) with InstantRewards with Obtained(owner)