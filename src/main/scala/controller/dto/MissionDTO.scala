package controller.dto

import controller.converters.ResourceConverters
import model.missions.Mission

import scala.annotation.tailrec

case class MissionDTO(cost: List[EffectDTO], rewards: List[EffectDTO], id: String)

object MissionDTO:
  def apply(mission: Mission): MissionDTO =
    MissionDTO(
      mission.cost.map(c =>
        EffectDTO(c)
      ),
      mission.reward.map(r =>
        EffectDTO(r)
      ),
      "placeholder-id"
    )