package controller

import controller.dto.{MissionDTO, PlayerBoardDTO, PlayerDTO}
import model.Players.Player
import model.effects.ResourceEffect
import model.missions.BaseMission
import model.resource.{Gold, MoonCrystal, PlayerBoard, SunCrystal}

import scala.util.Random

trait GameController:
  /**
   * Initialize a game with the given players
   *
   * @param playerList list of players who will participate in the game
   */
  def init(playerList: Seq[Player]): Unit

  /**
   * Resets the game to an empty state
   */
  def reset(): Unit

  /**
   * @return the sequence of current players, if the game as not been initialized is empty
   */
  def players: Seq[Player]

  /**
   * @return the missions in play, already sorted into their respective cells
   */
  def missions: Map[Int, List[MissionDTO]]

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
   * @param player the player proprietary of the board
   * @return the currentPlayerBoard of the given player
   */
  def playerBoard(player: Player): PlayerBoard

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
  private var _players: Seq[Player] = Seq.empty
  private var _playerBoards: Map[Player, PlayerBoard] = Map.empty
  private var _activePlayerIndex: Int = 0
  private var _currentRound: Int = 0
  var _missions: Map[Int, List[MissionDTO]] = Map.empty

  def init(playerList: Seq[Player]): Unit =
    require(playerList.length >= 2)
    reset()
    _players = Random.shuffle(playerList)
    _playerBoards = _players.map(p => (p, PlayerBoard.emptyBoard)).toMap //TODO initial gold

  def missions(): Map[Int, List[MissionDTO]] = _missions
  
  def reset(): Unit =
    _players = Seq.empty
    _playerBoards = Map.empty
    _activePlayerIndex = 0
    _currentRound = 0

  def players: Seq[PlayerDTO] = _players.map(PlayerDTO(_))

  def activePlayer: Option[PlayerDTO] =
    if _players.isEmpty then Option.empty
    else Option(players(_activePlayerIndex))

  def nonActivePlayerList: Seq[PlayerDTO] =
    require(_players.nonEmpty)
    players.filter(!_.equals(activePlayer.get))

  def playerBoard(playerName: String): PlayerBoardDTO =
    require(_players.exists(_.getName == playerName))
    PlayerBoardDTO(_playerBoards.map((p, b) => p.getName -> b)(playerName))

  def playerBoard(player: PlayerDTO): PlayerBoardDTO = playerBoard(player.name)

  def nextTurn(): Unit =
    _activePlayerIndex = if _activePlayerIndex + 1 >= players.length then 0 else _activePlayerIndex + 1
    if _activePlayerIndex == 0 then _currentRound = _currentRound + 1

  def currentRound: Int = _currentRound + 1

  def isGameEnded: Boolean = _currentRound >= maxNumberOfRounds

  def maxNumberOfRounds: Int = if _players.length == 3 then 10 else 9


