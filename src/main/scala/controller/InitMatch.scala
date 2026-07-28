package controller

import model.Players.*

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
  def build(): Unit

class MatchBuilderImpl(playerAmount: Int) extends MatchBuilder:
  var currentPlayers: List[Player] = List.empty

  override def addPlayer(player: Player): MatchBuilder =
    currentPlayers = currentPlayers.concat(List(player))
    this

  override def build(): Unit = ???
