package controller

import controller.dto.PlayerDTO
import model.{MatchBuilder, MatchBuilderImpl}
import model.Players.*

/**
 * A controller to handle communication with view about match initialization
 */
trait ControllerMatchInit:
  /**
   * Given a new player's info, it checks if it's correct and updates the match info if so
   * @param name the new player's name
   * @param color the new player's color
   */
  def updateMatchInfo(name: String, color: String): Unit

  /**
   *
   * @return true if the last entered player was added
   */
  def isLastPlayerValid: Boolean

  /**
   *
   * @return the currently added players
   */
  def currentPlayers: Seq[PlayerDTO]

  /**
   *
   * @return true if there are enough players to start the match
   */
  def enoughPlayers: Boolean

  /**
   *
   * @return true if no more players can be added to this match
   */
  def maxPlayers: Boolean

  /**
   * Resets this controller's previously saved info
   */
  def reset(): Unit

  /**
   *
   * @return the match builder to create a new match with the current players
   */
  def builder: MatchBuilder

object ControllerMatchInit:
  private class ControllerMatchInitImpl extends ControllerMatchInit:
    private val matchBuilder = MatchBuilderImpl()
    matchBuilder.reset()
    var isLastPlayerValid = true

    private def accept(newPlayer: Player): Boolean =
      newPlayer.name != "" && Color.values.contains(newPlayer.color) &&
      !matchBuilder.currentPlayers.exists(p => p.name == newPlayer.name || p.color == newPlayer.color)

    override def updateMatchInfo(name: String, color: String): Unit =
      val nextPlayer = Player(name, Color.valueOf(color))
      isLastPlayerValid = accept(nextPlayer)
      if isLastPlayerValid then matchBuilder.addPlayer(nextPlayer)

    override def reset(): Unit =
      matchBuilder.reset()
      isLastPlayerValid = true

    override def builder: MatchBuilder = matchBuilder

    override def enoughPlayers: Boolean = matchBuilder.currentPlayers.size > 1

    override def maxPlayers: Boolean = matchBuilder.currentPlayers.size == 4

    override def currentPlayers: Seq[PlayerDTO] = matchBuilder.currentPlayers.map(PlayerDTO(_))

  def apply(): ControllerMatchInit = ControllerMatchInitImpl()