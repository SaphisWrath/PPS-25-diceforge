package model.missions

import model.effects.{Effect, ResourceEffect}
import model.resource.{PlayerBoard, Resource}

trait Mission:
  def reward: List[Effect]
  def cost: List[Effect]
  def get(receiver: PlayerBoard): Unit

object Mission:
  def unapply(mission: Mission): (List[Effect], List[Effect]) = (mission.reward, mission.cost)

case class BaseMission(reward: List[Effect], cost: List[Effect]) extends Mission:
  override def get(receiver: PlayerBoard): Unit = cost.foreach(r => {
    import model.utils.ResourceEffectModules.SubtractResource
    r.setReceiver(receiver)
    r.resolve()
  })

trait InstantRewards extends Mission:
  abstract override def get(receiver: PlayerBoard): Unit =
    import model.utils.ResourceEffectModules.AddResource
    reward.foreach(r => 
      r.setReceiver(receiver)
      r.resolve()
    )
    super.get(receiver)

class InstantMission(reward: List[Effect], cost: List[Effect]) extends BaseMission(reward, cost) with InstantRewards