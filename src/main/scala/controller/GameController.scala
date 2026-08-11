package controller

import controller.ViewPublisher
import controller.ViewPublisher.ViewContext.*
import controller.converters.TurnStepConverter
import controller.dto.{DieDTO, EffectDTO, ItemDTO, MissionDTO, PlayerBoardDTO, PlayerDTO}
import controller.choices.{ChoiceController, DieChoiceAndRollController, EffectSolveController, FaceSwapController}
import controller.dto.MissionDTO.MissionHandler
import model.ModelPublisher.*
import model.{GameMatch, ModelPublisher}
import model.Players.Player
import model.effects.{EffectManager, ResourceEffect, Target}
import model.effects.Target.{All, Others, Self}
import model.resource.Gold
import model.turn.TurnManagers.TurnAction.{ActivateSupport, BuyExtraAction, CompleteDiceThrow, EndSupport, EndTurn, StandardAction}

trait GameController:

  /**The final initialization of the game
   *
   * Should be called at the start of a game
   */
  def startGame(): Unit

  /**The players participating at the game
   *
   * @return the sequence of current players
   */
  def players: Seq[PlayerDTO]

  /**The results of the dices of the players
   * Returns most recent die throws of selected player
   * @param playerName the player's name
   * @return the most recent die throws
   */
  def recentDiceResults(playerName: String): Seq[Option[EffectDTO]]

  /** The positions occupied by the players
   *
   * @return A map from the index of the position to the [[Player]] occupying it
   */
  def playerPositions: Map[Int, PlayerDTO]

  /**The map of the missions
   *
   * @return the missions in play, already sorted into their respective cells
   */
  def missions: Map[Int, Seq[MissionDTO]]

  /**The player currently playing his turn
   *
   * @return the currently active player
   */
  def activePlayer: PlayerDTO

  /**The player waiting for their turn
   *
   * @return the sequence of all players that are not the active player
   */
  def nonActivePlayerList: Seq[PlayerDTO]

  /**The board of a player
   *
   * Gives the board of a player corresponding to the DTO object
   * @param player the player proprietary of the board
   * @return the currentPlayerBoard of the given player
   */
  def playerBoard(player: PlayerDTO): PlayerBoardDTO = playerBoard(player.name)

  /**The board of a player
   *
   * Gives the board of a player with the corresponding name
   * @param playerName the name of the proprietary of the board
   * @return the currentPlayerBoard of the given player
   */
  def playerBoard(playerName: String): PlayerBoardDTO

  /**The missions obtained by a player
   *
   * Gives the obtained missions of a player corresponding to the DTO object
   * @param player the player proprietary of the missions
   * @return the missions obtained by the given player
   */
  def playerMissions(player: PlayerDTO): Seq[MissionDTO] = playerMissions(player.name)

  /**The missions obtained by a player
   *
   * Gives the obtained missions of a player with the corresponding name
   * @param playerName the name of the proprietary of the missions
   * @return the missions obtained by the given player
   */
  def playerMissions(playerName: String): Seq[MissionDTO]

  /**Check if the player can go to next turn
   *
   * @return true if the player can go to next turn, false otherwise
   */
  def canGoToNextTurn: Boolean

  /**Notify the game to go to next turn
   *
   * May do nothing if [[canGoToNextTurn]] is false
   *
   */
  def nextTurn(): Unit

  /**The current round
   * @return the current round number
   */
  def currentRound: Int

  /**Check if the game is ended
   *
   * @return true if the game ended, false otherwise
   */
  def isGameEnded: Boolean

  /**The total number of rounds that should be played
   * @return the maximum number of rounds of the currently initialized game
   */
  def maxNumberOfRounds: Int

  /** Notify the game that the dices are all thrown
   *
   */
  def endDiceThrow(): Unit

  /** Controller used for complicate effect resolutions
   * @return A instance of [[ChoiceController]] of [[EffectDTO]]
   */
  def solveController: ChoiceController[EffectDTO]

  /** Check if the active player can take an Action
   * @return true if the player already took his action, false otherwise
   */
  def canTakeAction: Boolean

  /** Check if the active player can buy an extra action
   *
   * @return true if the player can buy an extra action, false otherwise
   */
  def canBuyExtraAction: Boolean

  /** Check if the current turn phase is the SupportPhase
   * @return true if the current turn phase is the SupportPhase, false otherwise
   */
  def isSupportPhase: Boolean

  /** Notify the game to end the supportPhase
   * May do nothing if [[isSupportPhase]] is false
   */
  def endSupportPhase(): Unit

  /**Notify the game that the current player wants to buy an extra action
   * May do nothing if [[canBuyExtraAction]] is false
   */
  def buyExtraAction(): Unit

  /**Return the current turn step
   * @return A [[String]] representing the current turn step
   */
  def turnStep: String

  /**All the items of the shop
   *
   * @return A sequence of the [[ItemDTO]] representing the items of the shop
   */
  def shopItems: Seq[ItemDTO]

  /**The dices of the given player
   *
   * @param playerDTO The [[PlayerDTO]] instance corresponding to the player
   * @return The dices of the given player
   */
  def dice(playerDTO: PlayerDTO): Seq[DieDTO]

  /**A controller used for changing the faces of a die
   *
   * @param dieIndex the index of the die to modify
   * @return A [[ChoiceController]] instance
   */
  def faceSwapController(dieIndex: Int): ChoiceController[EffectDTO]

  /**A controller used to choose a single dice to throw
   *
   * @return A [[ChoiceController]] instance
   */
  def dieChoiceAndRollController: ChoiceController[DieDTO]

object GameController:
  private class GameControllerImpl(private val gameMatch: GameMatch) extends GameController with ModelSubscriber:
    this.setPublisher(ModelPublisher())

    override def update(context: ModelContext): Unit = context match
      case ModelContext.ResourceContext => ViewPublisher().notify(ResourceContext)
      case ModelContext.MissionContext => ViewPublisher().notify(MissionBoughtContext)
      case ModelContext.TurnEndContext => ViewPublisher().notify(TurnChangeContext)
      case ModelContext.TurnStepContext => ViewPublisher().notify(TurnStepChangeContext)
      case ModelContext.PlayerMovedContext => ViewPublisher().notify(PlayerMovedContext)
      case ModelContext.EffectChoiceContext => ViewPublisher().notify(PlayerChoiceContext)
      case ModelContext.FaceObtainedContext => ViewPublisher().notify(ItemObtainedContext)
      case ModelContext.DieChoiceContext => ViewPublisher().notify(SelectDieForThrowContext)
      case ModelContext.DiceThrownContext => ViewPublisher().notify(DiceThrownContext)
      case ModelContext.DiceThrowEnd => ViewPublisher().notify(DiceThrowEnd)

    override def startGame(): Unit =
      ViewPublisher().notify(TurnChangeContext)
      ViewPublisher().notify(TurnStepChangeContext)
      gameMatch.startDiceThrow()
      endDiceThrow()

    override def missions: Map[Int, Seq[MissionDTO]] =
      gameMatch.missions.map((i, list) => (i, list.map(m => MissionDTO(
        m,
        () => !m.canGet(extractTarget) || !gameMatch.isActionAvailable(StandardAction),
        () =>
          new MissionHandler(() =>
            m.get(extractTarget)
            gameMatch.executeAction(StandardAction)
          )
          gameMatch.movePlayer(gameMatch.activePlayer, i)
      ))))
      
    override def playerMissions(playerName: String): Seq[MissionDTO] =
      gameMatch.playerFrom(playerName) match
        case Some(player) => player.missions.map(m => MissionDTO(
          m,
          () => !m.canGet(extractTarget) || !gameMatch.isActionAvailable(ActivateSupport),
          () =>
            m.get(extractTarget)
            gameMatch.executeAction(ActivateSupport)
        ))
        case _ => Seq.empty

    private def extractTarget(target: Target): Seq[Player] = target match
      case Self => Seq(gameMatch.activePlayer)
      case All => gameMatch.players
      case Others => gameMatch.nonActivePlayers

    override def players: Seq[PlayerDTO] = gameMatch.players.map(PlayerDTO(_))

    override def recentDiceResults(playerName: String): Seq[Option[EffectDTO]] =
      gameMatch.playerFrom(playerName).get.dice.map(d => 
        if d.lastRolledEffect.isEmpty then None
        else Some(EffectDTO(d.lastRolledEffect.get))
      )
    
    override def playerPositions: Map[Int, PlayerDTO] = gameMatch.playerPositions.map((i, p) => (i, PlayerDTO(p)))

    override def activePlayer: PlayerDTO = PlayerDTO(gameMatch.activePlayer)

    override def nonActivePlayerList: Seq[PlayerDTO] = gameMatch.nonActivePlayers.map(PlayerDTO(_))

    override def playerBoard(playerName: String): PlayerBoardDTO = gameMatch.playerFrom(playerName) match
      case Some(player) => PlayerBoardDTO(player.board)
      case _ => throw IllegalArgumentException(s"$playerName does not correspond to any player") //TODO handle it better

    override def playerBoard(player: PlayerDTO): PlayerBoardDTO = playerBoard(player.name)

    override def canGoToNextTurn: Boolean = gameMatch.isActionAvailable(EndTurn)
    override def nextTurn(): Unit = gameMatch.executeAction(EndTurn)

    override def currentRound: Int = gameMatch.currentRound + 1

    override def isGameEnded: Boolean = gameMatch.isGameEnded

    override def maxNumberOfRounds: Int = gameMatch.maxNumberOfRounds

    override def isSupportPhase: Boolean = gameMatch.isActionAvailable(EndSupport)

    override def endSupportPhase(): Unit = gameMatch.executeAction(EndSupport)

    override def endDiceThrow(): Unit = gameMatch.executeAction(CompleteDiceThrow)

    override def canTakeAction: Boolean = !gameMatch.isActionAvailable(StandardAction)

    override def canBuyExtraAction: Boolean = gameMatch.isActionAvailable(BuyExtraAction)

    override def buyExtraAction(): Unit = gameMatch.executeAction(BuyExtraAction)

    override def turnStep: String = TurnStepConverter.toString(gameMatch.currentTurnStep)

    override def solveController: ChoiceController[EffectDTO] = EffectSolveController()

    override def shopItems: Seq[ItemDTO] =
      val shop = gameMatch.shop
      shop.items
        .map(i => (i, shop.getPrice(i)))
        .sortBy(_._2.getOrElse(Gold(0)).amount)
        .map((item, cost) => ItemDTO(
          EffectDTO(item),
          EffectDTO(ResourceEffect(cost.get, Target.Self)),
          shop.getStocked(item).getOrElse(0),
          () => {
            shop.buy(item, gameMatch.activePlayer)
            gameMatch.executeAction(StandardAction)
          },
          () => shop.getStocked(item).getOrElse(0) > 0 && gameMatch.activePlayer.board.canSpend(cost.get)
        ))

    override def dice(playerDTO: PlayerDTO): Seq[DieDTO] =
      gameMatch.playerFrom(playerDTO.name).get.dice.map(DieDTO(_))

    override def faceSwapController(dieIndex: Int): ChoiceController[EffectDTO] =
      FaceSwapController(
        gameMatch.activePlayer,
        gameMatch.activePlayer.dice(dieIndex)
      )

    override def dieChoiceAndRollController: ChoiceController[DieDTO] =
      DieChoiceAndRollController(gameMatch.activePlayer)

  def apply(gameMatch: GameMatch): GameController = GameControllerImpl(gameMatch)


