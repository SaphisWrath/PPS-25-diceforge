package controller

import controller.dto.PlayerDTO
import model.{MatchBuilder, MatchBuilderImpl}
import model.Players.*

/**
 * A controller to handle communication with view about match initialization
 */
trait ControllerMatchInit:
  def updateMatchInfo(name: String, color: String): Unit

  def isLastPlayerValid: Boolean
  
  def currentPlayers: Seq[PlayerDTO]

  def enoughPlayers: Boolean

  def maxPlayers: Boolean

  def reset(): Unit

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