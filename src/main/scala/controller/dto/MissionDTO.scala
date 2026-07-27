package controller.dto

import model.missions.{InstantMission, Mission}
  
case class MissionDTO(cost: List[EffectDTO], rewards: List[EffectDTO], id: String)

object MissionDTO:
  private val separator = ":"
  
  def apply(mission: Mission): MissionDTO = mission match
    case Mission(cost, reward, id) => MissionDTO (
      mission.cost.map(c => EffectDTO(c)),
      mission.reward.map(r => EffectDTO(r)),
      idBuilder(mission)
    )
    
  private def idBuilder(mission: Mission): String = mission match
    case _: InstantMission => "i" + separator + mission.id