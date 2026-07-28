package controller

import model.GameMatch
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
  def currentPlayers: Seq[Player]

  /**
   * A method that takes the gathered info and builds the match accordingly
   *
   * @return a new Match involving the previously added players
   */
  def build(): GameMatch

class MatchBuilderImpl(playerAmount: Int) extends MatchBuilder:
  var currentPlayers: Seq[Player] = Seq.empty

  override def addPlayer(player: Player): MatchBuilder =
    currentPlayers = currentPlayers.concat(Seq(player))
    this

  override def build(): GameMatch = GameMatch(currentPlayers)
