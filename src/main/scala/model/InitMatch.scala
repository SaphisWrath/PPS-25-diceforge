package model

import model.Players.*
import scala.util.Random

/**
 * A match that keeps track of rounds left and current player in action
 */
//trait Match:
//  /**
//   * @return how many rounds are left before the end of the game
//   */
//  def remainingRounds: Int
//
//  /**
//   * A method that passes the turn to the next player
//   */
//  def passTurn(): Unit
//
//  /**
//   * @return the player currently in control
//   */
//  def getCurrentPlayer: Player
//
//  /**
//   * @return true if the game is over, false otherwise
//   */
//  def gameOver: Boolean
//
//class MatchImpl(playerList: Iterable[Player]) extends Match:
//  private var playerOrder = Random.shuffle(playerList)
//  private var currentPlayer = playerOrder.head
//  private var turnCount = 0
//  var remainingRounds: Int = if playerOrder.size == 3 then 10 else 9
//
//  def passTurn(): Unit =
//    playerOrder = playerOrder.takeRight(playerOrder.size - 1)
//      .concat(Iterable(playerOrder.head))
//    currentPlayer = playerOrder.head
//    turnCount = turnCount + 1
//    if turnCount == playerOrder.size
//      then
//        turnCount = 0
//        remainingRounds = remainingRounds - 1
//
//  def getCurrentPlayer: Player = currentPlayer
//  def gameOver: Boolean = remainingRounds == 0

/**
 * A builder that creates a match after receiving enough players to start the game
 */
trait MatchBuilder:
  /**
   * A method that adds a player to the match
   *
   * @param player the player to be added to the game
   * @return a MatchBuilder with the newly added player
   */
  def addPlayer(player: Player): MatchBuilder

  /**
   * @return the currently added players
   */
  def currentPlayers: List[Player]

  /**
   * A method that takes the gathered info and builds the match accordingly
   *
   * @return a new Match involving the previously added players
   */
//  def build: Match

class MatchBuilderImpl(playerAmount: Int) extends MatchBuilder:
  var currentPlayers: List[Player] = List.empty

  override def addPlayer(player: Player): MatchBuilder =
    currentPlayers = currentPlayers.concat(List(player))
    this

//  override def build: Match =
//    MatchImpl(currentPlayers.take(playerAmount))
