package model

import model.Players.Player
import model.missions.{InstantMission, Mission, MissionMapBuilder}
import model.resource.{Gold, PlayerBoard}

import scala.util.Random

trait GameMatch:
  def missions: Map[Int, Seq[Mission]]

  def players: Seq[Player]

  def playerBoards: Seq[PlayerBoard]

  def activePlayer: Player

  def nonActivePlayers: Seq[Player]

  def playerFrom(name: String): Option[Player]

  def nextTurn(): Unit

  def currentTurn: Int

  def currentRound: Int

  def maxNumberOfRounds: Int

  def isGameEnded: Boolean

object GameMatch:
  private class GameMatchImpl(playerList: Seq[Player]) extends GameMatch:
    val players: Seq[Player] = Random.shuffle(playerList)
    private var turn: Int = 0
    private var round: Int = 0
    private val _missions: Map[Int, Seq[Mission]] = MissionMapBuilder.makePlaceholderMissions

    def missions: Map[Int, Seq[Mission]] = _missions

    def playerBoards: Seq[PlayerBoard] = players.map(_.board)

    override def activePlayer: Player = players(turn)

    override def nonActivePlayers: Seq[Player] = players.filter(_ != activePlayer)

    override def playerFrom(name: String): Option[Player] = players.find(_.name == name)

    override def nextTurn(): Unit =
      turn = turn + 1
      if turn == playerList.length then
        turn = 0
        round = round + 1

    override def currentTurn: Int = turn

    override def currentRound: Int = round

    override val maxNumberOfRounds: Int = if players.length == 3 then 10 else 9

    override def isGameEnded: Boolean = round >= maxNumberOfRounds

  def apply(playerList: Seq[Player]): GameMatch = GameMatchImpl(playerList)