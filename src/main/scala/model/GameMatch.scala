package model

import model.ModelPublisher.ModelContext
import model.ModelPublisher.ModelContext.{TurnEndContext, TurnStepContext}
import model.Players.Player
import model.dice.Die
import model.effects.{Effect, EffectManager}
import model.missions.{Mission, MissionMapBuilder}
import model.resource.{Gold, PlayerBoard, SunCrystal}
import model.shop.factories.EffectShopFactory
import model.shop.Shop
import model.turn.TurnManagers.TurnStep.StartStep
import model.turn.TurnManagers.{TurnAction, TurnManager, TurnStep}
import model.utils.RandomModules.given_RandomModule_Int

import scala.util.Random

trait GameMatch:
  /** The map of the missions in the game
   *
   * The map is divided in one or more "cells",
   * each one can contain one or more missions
   *
   * @return The map from Int to a Seq of [[Mission]]
   */
  def missions: Map[Int, Seq[Mission]]

  /** All players participating at the game
   *
   * @return A Seq containing all [[Player]] instances
   */
  def players: Seq[Player]

  /** All the boards of the players
   *
   * @return A Seq containing all [[PlayerBoard]] instances
   */
  def playerBoards: Seq[PlayerBoard]

  /** The currently active player
   *
   * @return The instance of [[Player]] relative to the active player
   */
  def activePlayer: Player

  /** All the currently non-active players
   *
   * @return The instances of [[Player]] relative to the non-active
   */
  def nonActivePlayers: Seq[Player]

  /** The player with the given name
   *
   * @param name the name of the player
   * @return A [[Some]] of [[Player]] if the player is present in the game, a [[None]] otherwise
   */
  def playerFrom(name: String): Option[Player]

  /** The current turn number
   *
   * @return An [[Int]] representing the current turn
   */
  def currentTurn: Int

  /** The current round number
   *
   * @return An [[Int]] representing the current round
   */
  def currentRound: Int

  /** The number of rounds after which the game is ended
   *
   * @return An [[Int]] representing maximum number of rounds
   */
  def maxNumberOfRounds: Int

  /** Check if the game has ended
   *
   * @return [[true]] if the game has ended, [[false]] otherwise
   */
  def isGameEnded: Boolean

  /** Check if a particular turn action can be done in this game state
   *
   * @param turnAction the turn action that should be checked the availability of
   * @return [[true]] if the action is available, [[false]] otherwise
   */
  def isActionAvailable(turnAction: TurnAction): Boolean

  /** Execute a particular turn action
   *
   * Execute the actions relative to the action, if the action is not available it does nothing.
   *
   * @param turnAction The action that should be executed
   */
  def executeAction(turnAction: TurnAction): Unit

  /** The current turn step
   *
   * @return The [[TurnStep]] instance
   */
  def currentTurnStep: TurnStep

  /** The position of the players in the map
   *
   * @return The map containing the players positions
   */
  def playerPositions: Map[Int, Player]

  /** Gives out the player in the given position, if any
   *
   * @param position The position that should be checked
   * @return A [[Some]] of the [[Player]] occupying the position if any, a [[None]] otherwise
   */
  def playerInPosition(position: Int): Option[Player]

  /** Moves a given player to a new position
   *
   * @param player      The player that should be moved
   * @param newPosition the
   */
  def movePlayer(player: Player, newPosition: Int): Unit

  /** Makes all the players throw their dices
   */
  def startDiceThrow(): Unit

  /** Makes the given players throw the given dices
   *
   * @param playerDice A seq containing the [[Player]] instances and the [[Die]] instances
   */
  def startDiceThrow(playerDice: Seq[(Player, Seq[Die])]): Unit

  /** Gives the results of the player dices
   *
   * @return The Seq of [[Player]] instances and their [[Effect]] results instances
   */
  def getDiceResults: Seq[(Player, Effect)]

  def shop: Shop[Effect]

object GameMatch:

  private def initializePlayerList(playerList: Seq[Player]): Seq[Player] =
    val list = Random.shuffle(playerList)
    list.foreach(p => p.board.gold = p.board.gold + Gold(3 - list.indexOf(p)))
    list

  private class GameMatchImpl(playerList: Seq[Player]) extends GameMatch:
    val players: Seq[Player] = initializePlayerList(playerList)
    private val _missions: Map[Int, Seq[Mission]] = MissionMapBuilder.makeStandardMissions(players.length)
    private val mapManager: MapManager = MapManager(
      () => ModelPublisher().notify(ModelContext.PlayerMovedContext),
      player => startDiceThrow(Seq((player, player.dice)))
    )
    export mapManager.*

    def missions: Map[Int, Seq[Mission]] = _missions

    def playerBoards: Seq[PlayerBoard] = players.map(_.board)

    override def activePlayer: Player = players(turn)

    override def nonActivePlayers: Seq[Player] = players.filter(_ != activePlayer)

    override def playerFrom(name: String): Option[Player] = players.find(_.name == name)

    override def currentTurn: Int = turn

    override def currentRound: Int = round

    override val maxNumberOfRounds: Int = if players.length == 3 then 10 else 9

    override def isGameEnded: Boolean = round >= maxNumberOfRounds

    private var turn: Int = 0
    private var round: Int = 0
    private var turnManager: TurnManager = TurnManager(
      currentStep = StartStep,
      actionRequirements = {
        case TurnAction.BuyExtraAction => activePlayer.board.sunCrystals.amount >= 2
      },
      additionalActionEffects = {
        case TurnAction.BuyExtraAction =>
          activePlayer.board.sunCrystals = activePlayer.board.sunCrystals - SunCrystal(2)
          ModelPublisher().notify(ModelContext.ResourceContext)
        case TurnAction.EndTurn =>
          nextTurn()
          startDiceThrow()
      },
      consecutiveActions = {
        case TurnAction.EndTurn => TurnAction.CompleteDiceThrow
        case TurnAction.CompleteDiceThrow if activePlayer.missions.isEmpty => TurnAction.EndSupport
      }
    )

    override def isActionAvailable(turnAction: TurnAction): Boolean =
      turnManager.isActionAvailable(turnAction)

    override def executeAction(turnAction: TurnAction): Unit = turnManager.executeAction(turnAction) match
      case Some(tm) =>
        turnManager = tm
        ModelPublisher().notify(TurnStepContext)
      case _ =>

    private def nextTurn(): Unit =
      activePlayer.missions.foreach(_.reset())
      turn = turn + 1
      if turn == playerList.length then
        turn = 0
        round = round + 1
      ModelPublisher().notify(TurnEndContext)

    override def currentTurnStep: TurnStep = turnManager.currentStep

    override def getDiceResults: Seq[(Player, Effect)] = players.flatMap(p => p.dice.map(d => (p, d.lastRolledEffect.get)))

    override def startDiceThrow(): Unit = startDiceThrow(players.map(p => (p, p.dice)))

    override def startDiceThrow(playerDice: Seq[(Player, Seq[Die])]): Unit =
      val thrown = playerDice.flatMap((p, d) => d.map(die => (p, die.roll)))
      EffectManager().attemptSolve(thrown, true)

    override val shop: Shop[Effect] = EffectShopFactory().makeStandardShop

  def apply(playerList: Seq[Player]): GameMatch = GameMatchImpl(playerList)