package mock

import model.Players.{Color, Player}
import model.missions.Mission
import model.resource.PlayerBoard

case class MockPlayer(name: String, color: Color, var board: PlayerBoard = PlayerBoard.emptyBoard) extends Player:
  override def addMission(mission: Mission): Unit = {}

  override def missions: Seq[Mission] = Seq.empty
