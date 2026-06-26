package model

import scala.util.Random

enum Color:
  case Orange, Blue, Green, Black
  
object Color:
  val colorMap = Map(("Orange", Orange), ("Blue", Blue), ("Green", Green), ("Black", Black))
  def stringToColor(color: String): Option[Color] = colorMap.get(color)

type Name = String
type Player = (Name, Color)

/**
 * A factory that creates players and makes sure new players don't share
 * the same name or colors as already created players
 */
trait PlayerFactory:
  /**
   * A method that creates a new player
   *
   * @param name the name of the player
   * @param color the color of the player
   * @return an Option containing the player if the name and color assigned aren't already in use,
   *         an empty Option otherwise
   */
  def create(name: Name, color: Color): Option[Player]

class PlayerFactoryImpl extends PlayerFactory:
  private var playerList: Seq[Player] = List.empty

  def create(name: Name, color: Color): Option[Player] =
    val newPlayer = (name, color)
    if playerList.map((pName, pColor) => pName).contains(name) ||
      playerList.map((pName, pColor) => pColor).contains(color)
      then Option.empty
    else
      playerList = playerList.concat(List(newPlayer))
      Some[Player](newPlayer)

/**
 * A match that keeps track of rounds left and current player in action
 */
trait Match:
  /**
   * @return how many rounds are left before the end of the game
   */
  def remainingRounds: Int

  /**
   * A method that passes the turn to the next player
   */
  def passTurn(): Unit

  /**
   * @return the player currently in control
   */
  def getCurrentPlayer: Player

  /**
   * @return true if the game is over, false otherwise
   */
  def gameOver: Boolean

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
   * A method that tells you if there is enough info to start the game
   *
   * @return true if build can be called, false otherwise
   */
  def ready: Boolean

  /**
   * A method that takes the gathered info and builds the match accordingly
   *
   * @return a new Match involving the previously added players
   */
  def build: Match

class MatchBuilderImpl(playerAmount: Int) extends MatchBuilder:
  private var playerList: Seq[Player] = List.empty

  def addPlayer(player: Player): MatchBuilder =
    playerList = playerList.concat(List(player))
    this

  def ready: Boolean =
    playerList.size >= playerAmount

  def build: Match = ???
