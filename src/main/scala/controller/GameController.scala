package controller

import controller.ViewPublisher.ViewContext.*
import controller.dto.{MissionDTO, PlayerBoardDTO, PlayerDTO}
import model.ModelPublisher.*
import model.{GameMatch, ModelPublisher}
import model.Players.Player
import model.effects.Target
import model.effects.Target.{All, Others, Self}
import model.resource.SunCrystal
import model.utils.ValueProperty
import model.dice.MockDieFactory.*
import model.turn.TurnManagers.TurnAction.{ActivateSupport, BuyExtraAction, CompleteDiceThrow, EndSupport, EndTurn, StandardAction}
import model.utils.TemporaryDie

trait GameController:

  def startGame(): Unit
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
  def playerBoard(player: PlayerDTO): PlayerBoardDTO = playerBoard(player.name)

  /**
   * @param playerName the name of the proprietary of the board
   * @return the currentPlayerBoard of the given player
   */
  def playerBoard(playerName: String): PlayerBoardDTO

  /**
   * @param player the player proprietary of the missions
   * @return the missions obtained by the given player
   */
  def playerMissions(player: PlayerDTO): Seq[MissionDTO] = playerMissions(player.name)

  /**
   * @param playerName the name of the proprietary of the missions
   * @return the missions obtained by the given player
   */
  def playerMissions(playerName: String): Seq[MissionDTO]
  
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

  def endDiceThrow(): Unit
  /**
   * @return true if the player already took his action, false otherwise
   */
  def canTakeAction: Boolean

  def canBuyExtraAction: Boolean
  
  def canEndSupportPhase: Boolean
  
  def endSupportPhase(): Unit

  def buyExtraAction(): Unit

  def turnStep: String

object GameController:
  private class GameControllerImpl(private val gameMatch: GameMatch) extends GameController with ModelSubscriber:
    this.setPublisher(ModelPublisher())

    override def update(context: ModelContext): Unit = context match
      case ModelContext.ResourceContext => ViewPublisher().notify(ResourceContext)
      case ModelContext.MissionContext => ViewPublisher().notify(MissionBoughtContext)
      case ModelContext.TurnEndContext => ViewPublisher().notify(TurnChangeContext)
      case ModelContext.TurnStepContext => ViewPublisher().notify(TurnStepChangeContext)

    override def startGame(): Unit =
      ViewPublisher().notify(TurnChangeContext)
      ViewPublisher().notify(TurnStepChangeContext)

    override def missions: Map[Int, Seq[MissionDTO]] =
      gameMatch.missions.map((i, list) => (i, list.map(m => MissionDTO(
        m,
        () => !m.canGet(extractTarget) || !gameMatch.isActionAvailable(StandardAction),
        () => {
          m.get(extractTarget)
          gameMatch.executeAction(StandardAction)
        }
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

    override def activePlayer: PlayerDTO = PlayerDTO(gameMatch.activePlayer)

    override def nonActivePlayerList: Seq[PlayerDTO] = gameMatch.nonActivePlayers.map(PlayerDTO(_))

    override def playerBoard(playerName: String): PlayerBoardDTO = gameMatch.playerFrom(playerName) match
      case Some(player) => PlayerBoardDTO(player.board)
      case _ => throw IllegalArgumentException(s"$playerName does not correspond to any player") //TODO handle it better

    override def playerBoard(player: PlayerDTO): PlayerBoardDTO = playerBoard(player.name)

    override def nextTurn(): Unit =
      if gameMatch.isActionAvailable(EndTurn) then
        gameMatch.executeAction(EndTurn)

    override def currentRound: Int = gameMatch.currentRound + 1

    override def isGameEnded: Boolean = gameMatch.isGameEnded

    override def maxNumberOfRounds: Int = gameMatch.maxNumberOfRounds

    override def diceThrowManager: DiceThrowManager = DiceThrowManager(gameMatch)
    
    override def canEndSupportPhase: Boolean = gameMatch.isActionAvailable(EndSupport)

    override def endSupportPhase(): Unit = gameMatch.executeAction(EndSupport)
    
    override def playerDice(player: PlayerDTO): Seq[TemporaryDie] = {
      val diceMap = gameMatch.players.map(p =>
        if gameMatch.players.indexOf(p) != 0
        then (PlayerDTO(p), Seq(mockCopyDie))
        else (PlayerDTO(p), Seq(mockOptionDie))
      ).toMap
      diceMap(player)
    }

    override def endDiceThrow(): Unit =
      gameMatch.executeAction(CompleteDiceThrow)

    override def canTakeAction: Boolean = !gameMatch.isActionAvailable(StandardAction)

    override def canBuyExtraAction: Boolean = gameMatch.isActionAvailable(BuyExtraAction)
    
    override def buyExtraAction(): Unit = gameMatch.executeAction(BuyExtraAction)

    override def turnStep: String = gameMatch.currentTurnStep.toString

  def apply(gameMatch: GameMatch): GameController = GameControllerImpl(gameMatch)


