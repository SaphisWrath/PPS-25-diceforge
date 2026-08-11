package controller

import controller.publishers.ViewPublisher.ViewContext.*
import controller.converters.{GameDTOConverter, TurnStepConverter}
import controller.dto.*
import controller.FaceSwapController
import controller.publishers.{ModelPublisherBridge, ViewPublisher}
import model.GameMatch
import controller.ViewPublisher
import controller.ViewPublisher.ViewContext.*
import controller.converters.TurnStepConverter
import controller.dto.{DieDTO, EffectDTO, ItemDTO, MissionDTO, PlayerBoardDTO, PlayerDTO}
import controller.choices.{ChoiceController, DieChoiceAndRollController, EffectSolveController, FaceSwapController}
import controller.dto.MissionDTO.MissionHandler
import model.ModelPublisher.*
import model.{GameMatch, ModelPublisher}
import model.Players.Player
import model.effects.Target
import model.effects.{EffectManager, ResourceEffect, Target}
import model.effects.Target.{All, Others, Self}
import model.turn.TurnManagers.TurnAction.*

trait GameController:

  /** The final initialization of the game
   *
   * Should be called at the start of a game
   */
  def startGame(): Unit

  /** The players participating at the game
   *
   * @return the sequence of current players
   */
  def players: Seq[PlayerDTO]

  /** The results of the dices of the players
   * Returns most recent die throws of selected player
   *
   * @param playerName the player's name
   * @return the most recent die throws
   */
  def recentDiceResults(playerName: String): Seq[Option[EffectDTO]]

  /** The positions occupied by the players
   *
   * @return A map from the index of the position to the [[Player]] occupying it
   */
  def playerPositions: Map[Int, PlayerDTO]

  /** The map of the missions
   *
   * @return the missions in play, already sorted into their respective cells
   */
  def missions: Map[Int, Seq[MissionDTO]]

  /** The player currently playing his turn
   *
   * @return the currently active player
   */
  def activePlayer: PlayerDTO

  /** The player waiting for their turn
   *
   * @return the sequence of all players that are not the active player
   */
  def nonActivePlayers: Seq[PlayerDTO]

  /** The board of a player
   *
   * Gives the board of a player corresponding to the DTO object
   *
   * @param player the player proprietary of the board
   * @return the currentPlayerBoard of the given player, if it doesn't find a player it returns an empty board
   */
  def playerBoard(player: PlayerDTO): PlayerBoardDTO = playerBoard(player.name)

  /** The board of a player
   *
   * Gives the board of a player with the corresponding name
   *
   * @param playerName the name of the proprietary of the board
   * @return the currentPlayerBoard of the given player
   */
  def playerBoard(playerName: String): PlayerBoardDTO

  /** The missions obtained by a player
   *
   * Gives the obtained missions of a player corresponding to the DTO object
   *
   * @param player the player proprietary of the missions
   * @return the missions obtained by the given player
   */
  def playerMissions(player: PlayerDTO): Seq[MissionDTO] = playerMissions(player.name)

  /** The missions obtained by a player
   *
   * Gives the obtained missions of a player with the corresponding name
   *
   * @param playerName the name of the proprietary of the missions
   * @return the missions obtained by the given player
   */
  def playerMissions(playerName: String): Seq[MissionDTO]

  /** Check if the player can go to next turn
   *
   * @return true if the player can go to next turn, false otherwise
   */
  def canGoToNextTurn: Boolean

  /** Notify the game to go to next turn
   *
   * May do nothing if [[canGoToNextTurn]] is false
   *
   */
  def nextTurn(): Unit

  /** The current round
   *
   * @return the current round number
   */
  def currentRound: Int

  /** Check if the game is ended
   *
   * @return true if the game ended, false otherwise
   */
  def isGameEnded: Boolean

  /** The total number of rounds that should be played
   *
   * @return the maximum number of rounds of the currently initialized game
   */
  def maxNumberOfRounds: Int

  /** Notify the game that the dices are all thrown
   *
   */
  def endDiceThrow(): Unit

  /** Controller used for complicate effect resolutions
   *
   * @return An instance of [[ChoiceController]] of [[EffectDTO]]
   */
  def solveController: ChoiceController[EffectDTO]

  /** Check if the active player can take an Action
   *
   * @return true if the player already took his action, false otherwise
   */
  def canTakeAction: Boolean

  /** Check if the active player can buy an extra action
   *
   * @return true if the player can buy an extra action, false otherwise
   */
  def canBuyExtraAction: Boolean

  /** Check if the current turn phase is the SupportPhase
   *
   * @return true if the current turn phase is the SupportPhase, false otherwise
   */
  def isSupportPhase: Boolean

  /** Notify the game to end the supportPhase
   * May do nothing if [[isSupportPhase]] is false
   */
  def endSupportPhase(): Unit

  /** Notify the game that the current player wants to buy an extra action
   * May do nothing if [[canBuyExtraAction]] is false
   */
  def buyExtraAction(): Unit

  /** Return the current turn step
   *
   * @return A [[String]] representing the current turn step
   */
  def turnStep: String

  /** All the items of the shop
   *
   * @return A sequence of the [[ItemDTO]] representing the items of the shop
   */
  def shopItems: Seq[ItemDTO]

  /** The dices of the given player
   *
   * @param playerDTO The [[PlayerDTO]] instance corresponding to the player
   * @return The dices of the given player
   */
  def dice(playerDTO: PlayerDTO): Seq[DieDTO]

  /** A controller used for changing the faces of a die
   *
   * @param dieIndex the index of the die to modify
   * @return A [[ChoiceController]] instance
   */
  def faceSwapController(dieIndex: Int): ChoiceController[EffectDTO]

  /** A controller used to choose a single dice to throw
   *
   * @return A [[ChoiceController]] instance
   */
  def dieChoiceAndRollController: ChoiceController[DieDTO]

object GameController:
  private class GameControllerImpl(private val gameMatch: GameMatch) extends GameController:
    private val gameDTOConverter: GameDTOConverter = GameDTOConverter(gameMatch)
    private val modelPublisherBridge: ModelPublisherBridge = ModelPublisherBridge()

    export gameMatch.{isGameEnded, maxNumberOfRounds}

    override def startGame(): Unit =
      ViewPublisher().notify(TurnChangeContext)
      ViewPublisher().notify(TurnStepChangeContext)
      gameMatch.startDiceThrow()
      endDiceThrow()

    override def missions: Map[Int, Seq[MissionDTO]] = gameDTOConverter.missionDTOs(extractTarget)

    override def playerMissions(playerName: String): Seq[MissionDTO] = gameDTOConverter.playerMissions(playerName, extractTarget)

    override def players: Seq[PlayerDTO] = gameDTOConverter.playersDTOs

    override def recentDiceResults(playerName: String): Seq[Option[EffectDTO]] = gameDTOConverter.recentDiceResultsDTO(playerName)

    override def playerPositions: Map[Int, PlayerDTO] = gameDTOConverter.playerPositionsDTO

    override def activePlayer: PlayerDTO = gameDTOConverter.activePlayerDTO

    override def nonActivePlayers: Seq[PlayerDTO] = gameDTOConverter.nonActivePlayersDTOs

    override def playerBoard(playerName: String): PlayerBoardDTO = gameDTOConverter.playerBoardDTO(playerName)

    override def shopItems: Seq[ItemDTO] = gameDTOConverter.shopItemsDTO

    override def playerBoard(player: PlayerDTO): PlayerBoardDTO = playerBoard(player.name)

    override def currentRound: Int = gameMatch.currentRound + 1

    override def canGoToNextTurn: Boolean = gameMatch.isActionAvailable(EndTurn)

    override def nextTurn(): Unit = gameMatch.executeAction(EndTurn)

    override def isSupportPhase: Boolean = gameMatch.isActionAvailable(EndSupport)

    override def endSupportPhase(): Unit = gameMatch.executeAction(EndSupport)

    override def endDiceThrow(): Unit = gameMatch.executeAction(CompleteDiceThrow)

    override def canTakeAction: Boolean = !gameMatch.isActionAvailable(StandardAction)

    override def canBuyExtraAction: Boolean = gameMatch.isActionAvailable(BuyExtraAction)

    override def buyExtraAction(): Unit = gameMatch.executeAction(BuyExtraAction)

    override def turnStep: String = TurnStepConverter.toString(gameMatch.currentTurnStep)

    override def solveController: ChoiceController[EffectDTO] = EffectSolveController()

    override def faceSwapController(dieIndex: Int): ChoiceController[EffectDTO] =
      FaceSwapController(
        gameMatch.activePlayer,
        gameMatch.activePlayer.dice(dieIndex)
      )

    override def dieChoiceAndRollController: ChoiceController[DieDTO] =
      DieChoiceAndRollController(gameMatch.activePlayer)

    private def extractTarget(target: Target): Seq[Player] = target match
      case Self => Seq(gameMatch.activePlayer)
      case All => gameMatch.players
      case Others => gameMatch.nonActivePlayers

  def apply(gameMatch: GameMatch): GameController = GameControllerImpl(gameMatch)


