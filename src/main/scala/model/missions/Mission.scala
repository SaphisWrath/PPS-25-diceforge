package model.missions

import model.effects.{Effect, ResourceEffect}
import model.resource.{PlayerBoard, Resource}

trait Mission:
  def reward: List[Resource]
  def cost: List[Resource]
  def get(receiver: PlayerBoard): Unit

object Mission:
  def unapply(mission: Mission): (List[Resource], List[Resource]) = (mission.reward, mission.cost)

case class BaseMission(reward: List[Resource], cost: List[Resource]) extends Mission:
  override def get(receiver: PlayerBoard): Unit = cost.foreach(r => {
    import model.utils.ResourceEffectModules.SubtractResource
    ResourceEffect(r, Option(receiver)).resolve()
  })

trait InstantRewards extends Mission:
  abstract override def get(receiver: PlayerBoard): Unit =
    import model.utils.ResourceEffectModules.AddResource
    reward.foreach(r => {
      ResourceEffect(r, Option(receiver)).resolve()
    })
    super.get(receiver)

class InstantMission(reward: List[Resource], cost: List[Resource]) extends BaseMission(reward, cost) with InstantRewards