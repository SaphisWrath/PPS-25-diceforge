package model

import controller.MatchBuilder
import model.GameMatch
import model.Players.*

/**
 * A builder that creates a match after receiving enough players to start the game
 */
trait MatchBuilder:
  /**
   * Reset the builder to his original state
   */
  def reset(): Unit

  /**
   * A method that adds a player to the match
   *
   * @param player the player to be added to the game
   */
  def addPlayer(player: Player): Unit

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

class MatchBuilderImpl extends MatchBuilder:
  var currentPlayers: Seq[Player] = Seq.empty

  override def addPlayer(player: Player): Unit =
    currentPlayers = currentPlayers.appended(player)

  override def build(): GameMatch = GameMatch(currentPlayers)

  override def reset(): Unit = currentPlayers = Seq.empty
