package controller.dto

import model.missions.Mission
  
case class MissionDTO(
                       cost: List[EffectDTO], 
                       rewards: List[EffectDTO], 
                       id: String,
                       clickable: () => Boolean,
                       onClick: () => Unit)

object MissionDTO:
  def apply(mission: Mission): MissionDTO =
    MissionDTO.apply(
      mission,
      () => false,
      () => {println(s"Clicked mission ${mission.id}")}
    )

  def apply(mission: Mission, clickable: () => Boolean, onClick: () => Unit): MissionDTO = mission match
    case Mission(cost, reward, id) => MissionDTO(
      mission.cost.map(c => EffectDTO(c)),
      mission.reward.map(r => EffectDTO(r)),
      id,
      clickable,
      onClick
    )