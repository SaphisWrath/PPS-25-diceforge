package controller

import controller.ViewPublishers.Context.{MissionBoughtContext, ResourceContext, TurnChangeContext}
import controller.ViewPublishers.ViewPublisher
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
  def missions: Map[Int, Seq[MissionDTO]]

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

  /**
   * @return true if the player already took his action, false otherwise
   */
  def hasTurnActionBeenTaken: Boolean

object GameController:
  private class GameControllerImpl(private val gameMatch: GameMatch) extends GameController:
    
    private var _hasTurnActionBeenTaken: Boolean = false

    override def missions: Map[Int, Seq[MissionDTO]] =
      gameMatch.missions.map((i, list) => (i, list.map(m => MissionDTO(
        m,
        () => !m.canGet(gameMatch.playerBoardOf(gameMatch.activePlayer)),
        () => {
          m.get(gameMatch.playerBoardOf(activePlayer.name))
          _hasTurnActionBeenTaken = true
          ViewPublisher.notify(ResourceContext)
          ViewPublisher.notify(MissionBoughtContext)
        }
      ))))

    override def players: Seq[PlayerDTO] = gameMatch.players.map(PlayerDTO(_))

    override def activePlayer: PlayerDTO = PlayerDTO(gameMatch.activePlayer)

    override def nonActivePlayerList: Seq[PlayerDTO] = gameMatch.nonActivePlayers.map(PlayerDTO(_))

    override def playerBoard(playerName: String): PlayerBoardDTO = PlayerBoardDTO(gameMatch.playerBoardOf(playerName))

    override def playerBoard(player: PlayerDTO): PlayerBoardDTO = playerBoard(player.name)

    override def nextTurn(): Unit =
      gameMatch.nextTurn()
      _hasTurnActionBeenTaken = false
      ViewPublisher.notify(TurnChangeContext)

    override def currentRound: Int = gameMatch.currentRound + 1

    override def isGameEnded: Boolean = gameMatch.isGameEnded

    override def maxNumberOfRounds: Int = gameMatch.maxNumberOfRounds

    override def hasTurnActionBeenTaken: Boolean = _hasTurnActionBeenTaken

  def apply(gameMatch: GameMatch): GameController = GameControllerImpl(gameMatch)


