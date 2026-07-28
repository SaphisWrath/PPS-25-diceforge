package controller

import controller.dto.{MissionDTO, PlayerBoardDTO, PlayerDTO}
import model.GameMatch
import model.Players.Player

trait GameController:
  /**
   * @return the sequence of current players
   */
  def players: Seq[PlayerDTO]

  /**
   * @return the missions in play, already sorted into their respective cells
   */
  def missions: Map[Int, List[MissionDTO]]

  /**
   * @return the current active player
   */
  def activePlayer: PlayerDTO

  /**
   * @return the sequence of all players that are not the active player
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

  def missions_=(missions: Map[Int, List[MissionDTO]]): Unit

object GameController:
  private class GameControllerImpl(playerList: Seq[Player]) extends GameController:
    private val gameMatch = GameMatch(playerList)
    private var _missions: Map[Int, List[MissionDTO]] = Map.empty

    override def missions_=(missions: Map[Int, List[MissionDTO]]): Unit = _missions = missions

    override def missions: Map[Int, List[MissionDTO]] = _missions

    override def players: Seq[PlayerDTO] = gameMatch.players.map(PlayerDTO(_))

    override def activePlayer: PlayerDTO = PlayerDTO(gameMatch.activePlayer)

    override def nonActivePlayerList: Seq[PlayerDTO] = gameMatch.nonActivePlayers.map(PlayerDTO(_))

    override def playerBoard(playerName: String): PlayerBoardDTO = PlayerBoardDTO(gameMatch.playerBoardOf(playerName))

    override def playerBoard(player: PlayerDTO): PlayerBoardDTO = playerBoard(player.name)

    override def nextTurn(): Unit = gameMatch.nextTurn()

    override def currentRound: Int = gameMatch.currentRound + 1

    override def isGameEnded: Boolean = gameMatch.isGameEnded

    override def maxNumberOfRounds: Int = gameMatch.maxNumberOfRounds

  def apply(playerList: Seq[Player]): GameController = GameControllerImpl(playerList)


