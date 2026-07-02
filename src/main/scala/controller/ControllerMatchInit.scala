package controller

import model.*
import model.Players.*

/**
 * A controller to handle communication with view about match initialization
 */
trait ControllerMatchInit:
  def setPlayerAmount(amount: Int): Unit
  def isPlayerAmountSet: Boolean
  def updateMatchInfo(name: String, color: String): Unit
  def isLastPlayerValid: Boolean
  def allPlayersSet: Boolean

class ControllerMatchInitImpl extends ControllerMatchInit:
  var isPlayerAmountSet = false
  var isLastPlayerValid = true
  private var matchBuilder: MatchBuilder = MatchBuilderImpl(2)
  private var playerAmount: Int = 0

  private def accept(newPlayer: Player): Boolean =
    !matchBuilder.currentPlayers.exists(p => p.getName == newPlayer.getName || p.getColor == newPlayer.getColor)

  override def setPlayerAmount(amount: Int): Unit =
    if !isPlayerAmountSet
    then
      matchBuilder = MatchBuilderImpl(amount)
      playerAmount = amount
      isPlayerAmountSet = true

  override def updateMatchInfo(name: String, color: String): Unit =
    val nextPlayer = Player(name, Color.valueOf(color))
    isLastPlayerValid = accept(nextPlayer)
    if isLastPlayerValid then matchBuilder = matchBuilder.addPlayer(nextPlayer)

  override def allPlayersSet: Boolean =
    matchBuilder.currentPlayers.size >= playerAmount