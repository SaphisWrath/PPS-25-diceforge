package model.missions

import model.dice.Effect
import model.dice.Effect.ResourceEffect

trait Mission:
  def reward: List[Effect]
  def cost: List[ResourceEffect]

object Mission:
  private class MissionImpl(var reward: List[Effect], var cost: List[ResourceEffect]) extends Mission
  def apply(rewards: List[Effect], cost: List[ResourceEffect]): Mission = MissionImpl(rewards, cost)
