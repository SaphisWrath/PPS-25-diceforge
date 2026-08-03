package controller.dto

import model.missions.{InstantMission, Mission, SupportMission}

enum MissionType:
  case Instant
  case Support
  case Base

object MissionType:
  def apply(m: Mission): MissionType = m match
    case m: SupportMission => MissionType.Support
    case m: InstantMission => MissionType.Instant
    case _ =>  MissionType.Base


case class MissionDTO(
  cost: List[EffectDTO],
  rewards: List[EffectDTO],
  id: String,
  clickable: () => Boolean,
  onClick: () => Unit,
  missionType: MissionType,
  count: Int,
  startingCount: Int
)

object MissionDTO:
  def apply(mission: Mission): MissionDTO =
    MissionDTO.apply(
      mission,
      () => false,
      () => {
        println(s"Clicked mission ${mission.id}")
      }
    )

  def apply(mission: Mission, clickable: () => Boolean, onClick: () => Unit): MissionDTO = mission match
    case Mission(cost, reward, id) => MissionDTO(
      mission.cost.map(c => EffectDTO(c)),
      mission.reward.map(r => EffectDTO(r)),
      id,
      clickable,
      onClick,
      MissionType(mission),
      mission.count,
      mission.startCount
    )