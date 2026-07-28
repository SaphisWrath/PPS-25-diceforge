package model

import model.Players.Player
import model.resource.PlayerBoard

import scala.util.Random

trait GameMatch:
  def players: Seq[Player]

  def playerBoards: Seq[PlayerBoard]

  def activePlayer: Player

  def nonActivePlayers: Seq[Player]

  def playerBoardOf(player: Player): PlayerBoard

  def playerBoardOf(playerName: String): PlayerBoard

  def nextTurn(): Unit

  def currentTurn: Int

  def currentRound: Int

  def maxNumberOfRounds: Int

  def isGameEnded: Boolean

object GameMatch:
  private class GameMatchImpl(playerList: Seq[Player]) extends GameMatch:
    private case class PlayerInformation(player: Player, turnOrder: Int, playerBoard: PlayerBoard)

    private val _players: Seq[PlayerInformation] =
      val shuffleList = Random.shuffle(playerList)
      shuffleList.map(p => PlayerInformation(p, shuffleList.indexOf(p), PlayerBoard.emptyBoard(p)))
    private var turn: Int = 0
    private var round: Int = 0

    def players: Seq[Player] = _players.map(_.player)

    def playerBoards: Seq[PlayerBoard] = _players.map(_.playerBoard)

    override def activePlayer: Player =
      _players.collect({ case PlayerInformation(player, turnOrder, _) if turnOrder == turn => player }).head

    override def nonActivePlayers: Seq[Player] =
      _players.collect({ case PlayerInformation(player, turnOrder, _) if turnOrder != turn => player })

    override def playerBoardOf(player: Player): PlayerBoard = playerBoardOf(player.getName)

    override def playerBoardOf(playerName: String): PlayerBoard =
      _players.collect({ case PlayerInformation(Player(name, _), _, b) if name == playerName => b }).head

    override def nextTurn(): Unit =
      turn = turn + 1
      if turn == playerList.length then
        turn = 0
        round = round + 1

    override def currentTurn: Int = turn

    override def currentRound: Int = round

    override val maxNumberOfRounds: Int = if _players.length == 3 then 10 else 9

    override def isGameEnded: Boolean = round >= maxNumberOfRounds

  def apply(playerList: Seq[Player]): GameMatch = GameMatchImpl(playerList)