package controller

import model.MatchBuilder
import model.Players.*

/**
 * A controller to handle communication with view about match initialization
 */
trait ControllerMatchInit:
  def updateMatchInfo(name: String, color: Color): Unit

  def isLastPlayerValid: Boolean

  def enoughPlayers: Boolean

  def maxPlayers: Boolean

  def reset(): Unit

  def builder: MatchBuilder

object ControllerMatchInit:
  private class ControllerMatchInitImpl(matchBuilder: MatchBuilder) extends ControllerMatchInit:
    matchBuilder.reset()
    var isLastPlayerValid = true

    private def accept(newPlayer: Player): Boolean =
      newPlayer.name != "" && Color.values.contains(newPlayer.color) &&
      !matchBuilder.currentPlayers.exists(p => p.name == newPlayer.name || p.color == newPlayer.color)

    override def updateMatchInfo(name: String, color: Color): Unit =
      val nextPlayer = Player(name, color)
      isLastPlayerValid = accept(nextPlayer)
      if isLastPlayerValid then matchBuilder.addPlayer(nextPlayer)

    override def reset(): Unit =
      matchBuilder.reset()
      isLastPlayerValid = true

    override def builder: MatchBuilder = matchBuilder

    override def enoughPlayers: Boolean = matchBuilder.currentPlayers.size > 1

    override def maxPlayers: Boolean = matchBuilder.currentPlayers.size == 4

  def apply(matchBuilder: MatchBuilder): ControllerMatchInit = ControllerMatchInitImpl(matchBuilder)