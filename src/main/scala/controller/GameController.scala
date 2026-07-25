package controller

import controller.dto.{MissionDTO, PlayerBoardDTO, PlayerDTO}
import model.Players.Player
import model.effects.ResourceEffect
import model.missions.BaseMission
import model.resource.{Gold, MoonCrystal, PlayerBoard, SunCrystal}

import scala.util.Random

trait GameController:
  /**
   * @return the sequence of current players, if the game as not been initialized is empty
   */
  def players: Seq[PlayerDTO]

  /**
   * @return the missions in play, already sorted into their respective cells
   */
  def missions: Map[Int, List[MissionDTO]]

  /**
   * @return an Option containing the current active player if the game as been initialized,
   *         empty otherwise
   */
  def activePlayer: Option[PlayerDTO]

  /**
   * @return the sequence of all players that are not the active player, if the game as not been initialized the
   *         sequence is empty
   */
  def nonActivePlayerList: Seq[PlayerDTO]

  /**
   * @param player the player proprietary of the board
   * @return the currentPlayerBoard of the given player
   */
  def playerBoard(player: PlayerDTO): PlayerBoardDTO

  /**
   * @param playerName the name of the proprietary of the board
   * @return the currentPlayerBoard of the given player
   */
  def playerBoard(playerName: String): PlayerBoardDTO

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
  private class GameControllerImpl(playerList: Seq[Player]) extends GameController:
    private var _players: Seq[Player] = Random.shuffle(playerList)
    private var _playerBoards: Map[Player, PlayerBoard] = 
      _players.map(p => (p, PlayerBoard.emptyBoard)).toMap //TODO initial gold
    private var _activePlayerIndex: Int = 0
    private var _currentRound: Int = 0
    var _missions: Map[Int, List[MissionDTO]] = Map.empty

    override def missions: Map[Int, List[MissionDTO]] = _missions

    override def players: Seq[PlayerDTO] = _players.map(PlayerDTO(_))

    override def activePlayer: Option[PlayerDTO] =
      if _players.isEmpty then Option.empty
      else Option(players(_activePlayerIndex))

    override def nonActivePlayerList: Seq[PlayerDTO] =
      require(_players.nonEmpty)
      players.filter(!_.equals(activePlayer.get))

    override def playerBoard(playerName: String): PlayerBoardDTO =
      require(_players.exists(_.getName == playerName))
      PlayerBoardDTO(_playerBoards.map((p, b) => p.getName -> b)(playerName))

    override def playerBoard(player: PlayerDTO): PlayerBoardDTO = playerBoard(player.name)

    override def nextTurn(): Unit =
      _activePlayerIndex = if _activePlayerIndex + 1 >= players.length then 0 else _activePlayerIndex + 1
      if _activePlayerIndex == 0 then _currentRound = _currentRound + 1

    override def currentRound: Int = _currentRound + 1

    override def isGameEnded: Boolean = _currentRound >= maxNumberOfRounds

    override def maxNumberOfRounds: Int = if _players.length == 3 then 10 else 9

  def apply(playerList: Seq[Player]): GameController = GameControllerImpl(playerList)


