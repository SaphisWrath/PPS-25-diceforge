package model

import model.Players.{Color, Player}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class GameMatchTest extends AnyFlatSpec with should.Matchers:
  val players: Seq[Player] = Seq(
    Player("P1", Color.Blue),
    Player("P2", Color.Black),
    Player("P3", Color.Green),
    Player("P4", Color.Orange),
  )
  def gameMatch(playerList: Seq[Player]): GameMatch = GameMatch(playerList)

  def nextRound(gameMatch: GameMatch): Unit =
    gameMatch.players.foreach(_ => gameMatch.nextTurn())

  "A match" should "have a list of players and playerBoards" in:
    val gm: GameMatch= gameMatch(players)
    gm.players should contain allElementsOf players
    gm.playerBoards should not be empty
    gm.playerBoards.length should be (players.length)

  it should "have an active player" in:
    val gm: GameMatch = gameMatch(players)
    gm.activePlayer should not be null

  it should "have a list of nonActivePlayers" in:
    val gm: GameMatch = gameMatch(players)
    val activePlayer = gm.activePlayer
    val nonActivePlayers = gm.nonActivePlayers
    nonActivePlayers should not contain activePlayer
    nonActivePlayers should contain allElementsOf players.filter(_ != activePlayer)

  it should "be able to go to next turn" in:
    val gm = gameMatch(players)
    val previousActivePlayer = gm.activePlayer
    val previousTurn = gm.currentTurn
    gm.nextTurn()
    gm.currentTurn should be (previousTurn+1)
    gm.activePlayer should not be previousActivePlayer

  it should "go to next round after all players took a turn" in:
    val gm = gameMatch(players)
    val previousRound = gm.currentRound
    players.foreach(_=>gm.nextTurn())
    gm.currentRound should be (previousRound+1)

  it should "end after some amount of rounds" in:
    val gm = gameMatch(players)
    gm.isGameEnded should be (false)
    Range(0, gm.maxNumberOfRounds).foreach(_=>nextRound(gm))
    gm.isGameEnded should be (true)

