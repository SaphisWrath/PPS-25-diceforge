package model.missions

import model.effects.{Effect, ResourceEffect}
import model.resource.{PlayerBoard, Resource}

trait Mission:
  def reward: List[Effect]
  def cost: List[ResourceEffect]
  def id: String
  def get(receiver: PlayerBoard): Unit
  def canGet(receiver: PlayerBoard): Boolean

object Mission:
  def unapply(mission: Mission): (List[Effect], List[ResourceEffect], String) = (mission.reward, mission.cost, mission.id)

case class BaseMission(reward: List[Effect], cost: List[ResourceEffect], id: String = "placeholder") extends Mission:
  override def get(receiver: PlayerBoard): Unit =
    if canGet(receiver) then
      cost.foreach(r => {
        r.setModule(model.utils.ResourceEffectModules.SubtractResource)
        r.setReceiver(receiver)
        r.resolve()
      })

  override def canGet(receiver: PlayerBoard): Boolean = cost.forall(e => receiver.canSpend(e.resource))

trait InstantRewards extends Mission:
  abstract override def get(receiver: PlayerBoard): Unit =
    if canGet(receiver) then
      reward.foreach {
        case res@ResourceEffect(_, _, _) =>
          res.setReceiver(receiver)
          res.resolve()
      }
      super.get(receiver)

class InstantMission(reward: List[Effect], cost: List[ResourceEffect], id: String = "placeholder") 
  extends BaseMission(reward, cost, id) with InstantRewards