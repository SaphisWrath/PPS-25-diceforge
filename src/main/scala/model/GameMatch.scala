package model

import model.ModelPublisher.ModelContext
import model.ModelPublisher.ModelContext.{TurnEndContext, TurnStepContext}
import model.Players.Player
import model.missions.{Mission, MissionMapBuilder}
import model.resource.{Gold, PlayerBoard}
import model.turn.TurnManagers.TurnAction.EndSupport
import model.turn.TurnManagers.TurnStep.{MainActionStep, StartStep, SupportStep}
import model.turn.TurnManagers.{TurnAction, TurnManager, TurnStep}

import scala.util.Random

trait GameMatch:
  def missions: Map[Int, Seq[Mission]]

  def players: Seq[Player]

  def playerBoards: Seq[PlayerBoard]

  def activePlayer: Player

  def nonActivePlayers: Seq[Player]

  def playerFrom(name: String): Option[Player]

  def currentTurn: Int

  def currentRound: Int

  def maxNumberOfRounds: Int

  def isGameEnded: Boolean

  def isActionAvailable(turnAction: TurnAction): Boolean

  def executeAction(turnAction: TurnAction): Unit

  def currentTurnStep: TurnStep

  def playerPositions: Map[Int, Player]

  def playerInPosition(position: Int): Option[Player]

  def movePlayer(player: Player, newPosition: Int): Unit

object GameMatch:
  private class GameMatchImpl(playerList: Seq[Player]) extends GameMatch:
    val players: Seq[Player] =
      val list = Random.shuffle(playerList)
      list.foreach(p => p.board.gold = p.board.gold + Gold(3-list.indexOf(p)))
      list
    private var turn: Int = 0
    private var round: Int = 0
    private val _missions: Map[Int, Seq[Mission]] = MissionMapBuilder.makePlaceholderMissions
    private var turnManager: TurnManager = TurnManager(StartStep)
    private val mapManager: MapManager = MapManager(
      player =>
        //TODO fai il lancio dei dadi
        ModelPublisher().notify(ModelContext.PlayerMovedContext)
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

    override def isActionAvailable(turnAction: TurnAction): Boolean =
      turnManager.isActionAvailable(turnAction) && otherParameters(turnAction)

    override def executeAction(turnAction: TurnAction): Unit = turnManager.executeAction(turnAction) match
      case Some(tm) =>
        if otherParameters(turnAction) then
          turnManager = tm
          otherActions(turnAction)
          ModelPublisher().notify(TurnStepContext)
      case _ =>
      
    private def otherParameters(turnAction: TurnAction): Boolean = turnAction match
      case TurnAction.BuyExtraAction => activePlayer.board.sunCrystals.amount >= 2
      case _ => true

    private def otherActions(turnAction: TurnAction): Unit = turnAction match
      case TurnAction.EndTurn => nextTurn()
      case TurnAction.CompleteDiceThrow => if activePlayer.missions.isEmpty then this.executeAction(EndSupport)
      case _ =>

    private def nextTurn(): Unit =
      activePlayer.missions.foreach(_.reset)
      turn = turn + 1
      if turn == playerList.length then
        turn = 0
        round = round + 1
      ModelPublisher().notify(TurnEndContext)
      
    override def currentTurnStep: TurnStep = turnManager.currentStep


  def apply(playerList: Seq[Player]): GameMatch = GameMatchImpl(playerList)