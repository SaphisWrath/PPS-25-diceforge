package model.missions

import model.effects.{Effect, ResourceEffect}
import model.resource.{PlayerBoard, Resource}

trait Mission:
  def reward: List[Effect]
  def cost: List[ResourceEffect]
  def get(receiver: PlayerBoard): Unit
  def canGet(receiver: PlayerBoard): Boolean

object Mission:
  def unapply(mission: Mission): (List[Effect], List[ResourceEffect]) = (mission.reward, mission.cost)

case class BaseMission(reward: List[Effect], cost: List[ResourceEffect]) extends Mission:
  override def get(receiver: PlayerBoard): Unit =
    if canGet(receiver) then
      cost.foreach(r => {
        import model.utils.ResourceEffectModules.SubtractResource
        r.setReceiver(receiver)
        r.resolve()
      })

  override def canGet(receiver: PlayerBoard): Boolean = cost.forall(e => receiver.canSpend(e.resource))

trait InstantRewards extends Mission:
  abstract override def get(receiver: PlayerBoard): Unit =
    if canGet(receiver) then
      import model.utils.ResourceEffectModules.AddResource
      reward.foreach(r =>
        r.setReceiver(receiver)
        r.resolve()
      )
      super.get(receiver)

class InstantMission(reward: List[Effect], cost: List[ResourceEffect]) extends BaseMission(reward, cost) with InstantRewards