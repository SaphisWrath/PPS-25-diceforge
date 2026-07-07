package controller

import model.Players.Player

import scala.util.Random

object GameController:
  private var _players: Seq[Player] = Seq()
  private var _activePlayerIndex: Int = 0
  private var _currentRound: Int = 0

  def init(playerList: Seq[Player]): Unit =
    require(playerList.length >= 2)
    _players = Random.shuffle(playerList)
    _activePlayerIndex = 0

  def reset(): Unit =
    _players = Seq()
    _activePlayerIndex = 0
    _currentRound = 0

  def players: Seq[Player] = _players

  def activePlayer: Option[Player] =
    if _players.isEmpty then Option.empty
    else Option(_players(_activePlayerIndex))

  def nonActivePlayerList: Seq[Player] =
    require(_players.nonEmpty)
    _players.filter(!_.equals(activePlayer.get))

  def nextTurn(): Unit =
    _activePlayerIndex = (_activePlayerIndex + 1) % _players.length //TODO
    if _activePlayerIndex == 0 then _currentRound = _currentRound + 1

  def currentRound: Int = _currentRound + 1

  def isGameEnded: Boolean = _currentRound >= maxNumberOfRounds

  def maxNumberOfRounds: Int = if _players.length == 3 then 10 else 9


