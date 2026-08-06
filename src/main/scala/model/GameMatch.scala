package model

import controller.{DiceThrowManager, PlayerChoice}
import controller.dto.{EffectDTO, PlayerDTO}
import model.ModelPublisher.ModelContext.{ChoiceContext, TurnEndContext, TurnStepContext}
import model.Players.Player
import model.dice.Die
import model.missions.{Mission, MissionMapBuilder}
import model.resource.{Gold, PlayerBoard}
import model.turn.TurnManagers.TurnAction.{CompleteDiceThrow, EndSupport}
import model.turn.TurnManagers.TurnStep.StartStep
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

  def startDiceThrow(): Unit

  def startDiceThrow(playerDice: Seq[(Player, Seq[Die])]): Unit

  def pendingChoices[A]: Seq[PlayerChoice[A]]

  def resumeAfterChoices[A](results: Seq[(PlayerDTO, A)]): Unit

  def getDiceResults: Seq[(PlayerDTO, EffectDTO)]

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
      case TurnAction.EndTurn =>
        nextTurn()
        startDiceThrow()
        this.executeAction(CompleteDiceThrow)
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

    private val diceThrowManager = DiceThrowManager(this)
    private var _pendingChoices: Seq[PlayerChoice[Any]] = Seq.empty
    private var resultCallback: Seq[(PlayerDTO, Any)] => Unit = _ => {}
    override def getDiceResults: Seq[(PlayerDTO, EffectDTO)] = diceThrowManager.allRawEffects

    override def startDiceThrow(): Unit = startDiceThrow(players.map(p => (p, p.dice)))

    override def startDiceThrow(playerDice: Seq[(Player, Seq[Die])]): Unit =
      _pendingChoices = diceThrowManager.copyEffectsFromRoll(playerDice)
      resultCallback = r => continueDiceThrow(r.asInstanceOf[Seq[(PlayerDTO, EffectDTO)]])
      ModelPublisher().notify(ChoiceContext)

    private def continueDiceThrow(resolvedChoices: Seq[(PlayerDTO, EffectDTO)]): Unit =
      _pendingChoices = diceThrowManager.optionEffectsFromRoll(resolvedChoices)
      resultCallback = r => diceThrowManager.endRoll(r.asInstanceOf[Seq[(PlayerDTO, EffectDTO)]])
      ModelPublisher().notify(ChoiceContext)

    override def pendingChoices[A]: Seq[PlayerChoice[A]] = _pendingChoices.asInstanceOf[Seq[PlayerChoice[A]]]

    override def resumeAfterChoices[A](results: Seq[(PlayerDTO, A)]): Unit = resultCallback(results)

  def apply(playerList: Seq[Player]): GameMatch = GameMatchImpl(playerList)