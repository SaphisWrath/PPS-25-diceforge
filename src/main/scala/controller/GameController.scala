package controller

import controller.ViewPublishers.Context.{MissionBoughtContext, TurnChangeContext}
import controller.ViewPublishers.ViewPublisher
import controller.converters.ResourceConverters
import controller.dto.{EffectDTO, MissionDTO, PlayerBoardDTO, PlayerDTO}
import model.GameMatch
import model.Players.Player
import model.dice.MockDieFactory.{mockGoldDie, mockOptionDie}
import model.utils.{DiceThrow, TemporaryDie}

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
   * @return the DiceThrowManager
   */
  def diceThrowManager: DiceThrowManager

  def playerDice(player: PlayerDTO): Seq[TemporaryDie]

object GameController:
  private class GameControllerImpl(private val gameMatch: GameMatch) extends GameController:
    override def missions: Map[Int, Seq[MissionDTO]] =
      gameMatch.missions.map((i, list) => (i, list.map(m => MissionDTO(
        m,
        () => !m.canGet(gameMatch.playerBoardOf(gameMatch.activePlayer)),
        () => {
          m.get(gameMatch.playerBoardOf(activePlayer.name))
          ViewPublisher.notifyResourceChange()
          ViewPublisher.notify(MissionBoughtContext)
        }
      ))))
      
    override def players: Seq[PlayerDTO] = gameMatch.players.map(PlayerDTO(_))

    override def activePlayer: PlayerDTO = PlayerDTO(gameMatch.activePlayer)

    override def nonActivePlayerList: Seq[PlayerDTO] = gameMatch.nonActivePlayers.map(PlayerDTO(_))

    override def playerBoard(playerName: String): PlayerBoardDTO = PlayerBoardDTO(gameMatch.playerBoardOf(playerName))

    override def playerBoard(player: PlayerDTO): PlayerBoardDTO = playerBoard(player.name)

    override def nextTurn(): Unit = {
      gameMatch.nextTurn()
      ViewPublisher.notify(TurnChangeContext)
    }

    override def currentRound: Int = gameMatch.currentRound + 1

    override def isGameEnded: Boolean = gameMatch.isGameEnded

    override def maxNumberOfRounds: Int = gameMatch.maxNumberOfRounds

    override def diceThrowManager: DiceThrowManager = DiceThrowManager(gameMatch)

    override def playerDice(player: PlayerDTO): Seq[TemporaryDie] = {
      val diceMap = gameMatch.players.map(p =>
        (PlayerDTO(p), Seq(mockOptionDie(gameMatch.playerBoardOf(p))))
      ).toMap

      diceMap(player)
    }


  def apply(gameMatch: GameMatch): GameController = GameControllerImpl(gameMatch)


