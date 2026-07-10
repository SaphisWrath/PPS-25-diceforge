package model.missions

import model.effects.Effect

trait Mission:
  def reward: List[Effect]
  def cost: List[Effect]
  def get(): Unit

object Mission:
  def unapply(mission: Mission): (List[Effect], List[Effect]) = (mission.reward, mission.cost)

case class BaseMission(reward: List[Effect], cost: List[Effect]) extends Mission:
  override def get(): Unit = cost.foreach(e => e.resolve())

trait InstantRewards extends Mission:
  abstract override def get(): Unit =
    reward.foreach(e => e.resolve())
    super.get()

class InstantMission(reward: List[Effect], cost: List[Effect]) extends BaseMission(reward, cost) with InstantRewards