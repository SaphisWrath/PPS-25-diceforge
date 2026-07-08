package controller

import model.Players.Player

import scala.util.Random

trait GameController:
  /**
   * Initialize a game with the given players
   * @param playerList
   */
  def init(playerList: Seq[Player]): Unit

  /**
   *  Resets the game to an empty state
   */
  def reset(): Unit

  /**
   * @return the sequence of current players, if the game as not been initialized is empty
   */
  def players: Seq[Player]

  /**
   * @return an Option containing the current active player if the game as been initialized,
   *         empty otherwise
   */
  def activePlayer: Option[Player]

  /**
   * @return the sequence of all players that are not the active player, if the game as not been initialized the
   *         sequence is empty
   */
  def nonActivePlayerList: Seq[Player]

  /**
   * Notify the game to go to the next turn
   */
  def nextTurn(): Unit

  /**
   * @return the current round number
   */
  def currentRound: Int

  /**
   * @return true if the game ended
   */
  def isGameEnded: Boolean

  /**
   * @return the maximum number of rounds of the currently initialized game
   */
  def maxNumberOfRounds: Int

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


