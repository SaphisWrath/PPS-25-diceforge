package model.missions

import model.Players.Player
import model.effects.{Effect, ResourceEffect, Target}
import model.resource.{PlayerBoard, Resource}

import javax.sound.midi.Receiver

trait Mission:
  def reward: List[Effect]
  def cost: List[ResourceEffect]
  def id: String
  def get(receiverProducer: Target => Seq[Player]): Unit
  def canGet(receiverProducer: Target => Seq[Player]): Boolean

object Mission:
  def unapply(mission: Mission): (List[Effect], List[ResourceEffect], String) = (mission.reward, mission.cost, mission.id)

abstract case class BaseMission(reward: List[Effect], cost: List[ResourceEffect], id: String = "placeholder") extends Mission:
  override def get(receiverProducer: Target => Seq[Player]): Unit =
    if canGet(receiverProducer) then
      cost.foreach(r => {
        r.setModule(model.utils.ResourceEffectModules.SubtractResource)
        r.resolve(receiverProducer(r.target))
      })
      obtainReward(receiverProducer)
      
  protected def obtainReward(receiverProducer: Target => Seq[Player]): Unit
      
  override def canGet(receiverProducer: Target => Seq[Player]): Boolean =
    cost.flatMap(e => receiverProducer(e.target).map(_.board.canSpend(e.resource))).reduce(_&&_)

trait InstantRewards extends BaseMission:
  override def obtainReward(receiverProducer: Target => Seq[Player]): Unit =
    if canGet(receiverProducer) then
      reward.foreach {
        case res@ResourceEffect(_, _) =>
          res.resolve(receiverProducer(res.target))
      }
      super.get(receiverProducer)
      
trait SupportRewards(supportCost: List[ResourceEffect]) extends BaseMission:
  override def obtainReward(receiverProducer:  Target => scala.Seq[Player]): Unit = 
    super.get(receiverProducer)
    val player = receiverProducer(Target.Self).head
    player.addMission(ObtainedMission(reward, supportCost, player, id))
    
trait Obtained(owner: Player) extends BaseMission:
  override def get(receiverProducer: Target => scala.Seq[Player] = _ => Seq(owner)): Unit = super.get(receiverProducer)
  
class InstantMission(reward: List[Effect], cost: List[ResourceEffect], id: String = "placeholder") 
  extends BaseMission(reward, cost, id) with InstantRewards

class SupportMission(reward: List[Effect], supportCost: List[ResourceEffect] , missionCost: List[ResourceEffect], id: String = "placeholder")
  extends BaseMission(reward, missionCost, id) with SupportRewards(supportCost)

class ObtainedMission(rewards: List[Effect], cost: List[ResourceEffect], owner: Player, id: String = "placeholder")
  extends BaseMission(rewards, cost, id) with InstantRewards with Obtained(owner)