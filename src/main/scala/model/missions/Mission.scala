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

case class BaseMission(reward: List[Effect], cost: List[ResourceEffect], id: String = "placeholder") extends Mission:
  override def get(receiverProducer: Target => Seq[Player]): Unit =
    if canGet(receiverProducer) then
      cost.foreach(r => {
        r.setModule(model.utils.ResourceEffectModules.SubtractResource)
        r.resolve(receiverProducer(r.target))
      })
      
  override def canGet(receiverProducer: Target => Seq[Player]): Boolean =
    cost.flatMap(e => receiverProducer(e.target).map(_.board.canSpend(e.resource))).reduce(_&&_)

trait InstantRewards extends Mission:
  abstract override def get(receiverProducer: Target => Seq[Player]): Unit =
    if canGet(receiverProducer) then
      reward.foreach {
        case res@ResourceEffect(_, _) =>
          res.resolve(receiverProducer(res.target))
      }
      super.get(receiverProducer)

class InstantMission(reward: List[Effect], cost: List[ResourceEffect], id: String = "placeholder") 
  extends BaseMission(reward, cost, id) with InstantRewards