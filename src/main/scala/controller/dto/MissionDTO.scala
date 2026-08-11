package controller.dto

import model.ModelPublisher
import model.ModelPublisher.{ModelContext, ModelSubscriber}
import model.effects.EffectManager
import model.missions.{InstantMission, LimitedPurchase, Mission, SupportMission}
import model.turn.TurnManagers.TurnAction.StandardAction

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
  purchaseCount: Int,
  startingPurchaseCount: Int
)

object MissionDTO:
  class MissionHandler(action: () => Unit) extends ModelSubscriber:
    setPublisher(ModelPublisher())
    private var playerMoved = false

    private def activateMission(): Unit =
      action()
      ModelPublisher().unsubscribe(this)

    override def update(context: ModelPublisher.ModelContext): Unit = context match
      case ModelPublisher.ModelContext.PlayerMovedContext =>
        if EffectManager().effectsToSolve.isEmpty
        then activateMission()
        else playerMoved = true
      case ModelPublisher.ModelContext.DiceThrowEnd => if playerMoved then activateMission()
      case _ =>

  def apply(mission: Mission): MissionDTO =
    MissionDTO.apply(
      mission,
      () => false,
      () => {
        println(s"Clicked mission ${mission.id}")
      }
    )

  def apply(mission: Mission, clickable: () => Boolean, onClick: () => Unit): MissionDTO = mission match
    case m: LimitedPurchase => MissionDTO(
      mission.cost.map(c => EffectDTO(c)),
      mission.reward.map(r => EffectDTO(r)),
      mission.id,
      clickable,
      onClick,
      MissionType(mission),
      m.purchaseCount,
      m.startingPurchaseCount
    )
    
    case Mission(cost, reward, id) => MissionDTO(
      mission.cost.map(c => EffectDTO(c)),
      mission.reward.map(r => EffectDTO(r)),
      id,
      clickable,
      onClick,
      MissionType(mission),
      0,
      0
    )