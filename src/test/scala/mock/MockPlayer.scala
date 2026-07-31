package mock

import model.Players.{Color, Player}
import model.missions.Obtained
import model.resource.PlayerBoard

case class MockPlayer(
                       name: String,
                       color: Color,
                       var board: PlayerBoard = PlayerBoard.emptyBoard,
                       private var _missions: Seq[Obtained] = Seq.empty
                     ) extends Player:
  override def addMission(mission: Obtained): Unit =
    _missions = _missions.appended(mission)

  override def missions: Seq[Obtained] = _missions

  def resetMissions(): Unit = _missions = Seq.empty
