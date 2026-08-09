package model

import model.ModelPublisher.ModelContext
import model.ModelPublisher.ModelContext.{DiceThrownContext, TurnEndContext, TurnStepContext}
import model.Players.Player
import model.dice.Die
import model.effects.{Effect, EffectManager}
import model.missions.{Mission, MissionMapBuilder}
import model.resource.{Gold, PlayerBoard}
import model.turn.TurnManagers.TurnAction.{CompleteDiceThrow, EndSupport}
import model.turn.TurnManagers.TurnStep.StartStep
import model.turn.TurnManagers.{TurnAction, TurnManager, TurnStep}
import model.utils.RandomModules.given_RandomModule_Int

import scala.util.Random
//TODO: Add ScalaDoc
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

  def startDiceThrow(): Unit

  def startDiceThrow(playerDice: Seq[(Player, Seq[Die])]): Unit

  def getDiceResults: Seq[(Player, Effect)]

//TODO Refactor
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
      StartStep,
      {
        case TurnAction.BuyExtraAction => activePlayer.board.sunCrystals.amount >= 2
      },
      {
        case TurnAction.EndTurn =>
          nextTurn()
          startDiceThrow()
          TurnAction.CompleteDiceThrow
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

    override def getDiceResults: Seq[(Player, Effect)] = players.flatMap(p => p.dice.map(d => (p, d.lastEffect.get)))

    override def startDiceThrow(): Unit = startDiceThrow(players.map(p => (p, p.dice)))

    override def startDiceThrow(playerDice: Seq[(Player, Seq[Die])]): Unit =
      val thrown = playerDice.flatMap((p, d) => d.map(die => (p, die.roll)))
      ModelPublisher().notify(DiceThrownContext)
      EffectManager().attemptSolve(thrown)


  def apply(playerList: Seq[Player]): GameMatch = GameMatchImpl(playerList)