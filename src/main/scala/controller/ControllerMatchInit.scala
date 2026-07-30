package controller

import model.*
import model.Players.*

/**
 * A controller to handle communication with view about match initialization
 */
trait ControllerMatchInit:
  def setPlayerAmount(amount: Int): Unit
  def isPlayerAmountSet: Boolean
  def updateMatchInfo(name: String, color: Color): Unit
  def isLastPlayerValid: Boolean
  def allPlayersSet: Boolean
  def reset(): Unit
  def builder: MatchBuilder

object ControllerMatchInit:
  private class ControllerMatchInitImpl(matchBuilder: MatchBuilder) extends ControllerMatchInit:
    matchBuilder.reset()
    var isPlayerAmountSet = false
    var isLastPlayerValid = true
    private var playerAmount: Int = 0
  
    private def accept(newPlayer: Player): Boolean =
      !matchBuilder.currentPlayers.exists(p => p.name == newPlayer.name || p.color == newPlayer.color)
  
    override def setPlayerAmount(amount: Int): Unit =
      if !isPlayerAmountSet
      then
        playerAmount = amount
        isPlayerAmountSet = true
  
    override def updateMatchInfo(name: String, color: Color): Unit =
      val nextPlayer = Player(name, color)
      isLastPlayerValid = accept(nextPlayer)
      if isLastPlayerValid then matchBuilder.addPlayer(nextPlayer)
  
    override def allPlayersSet: Boolean =
      matchBuilder.currentPlayers.size >= playerAmount
  
    override def reset(): Unit =
      matchBuilder.reset()
      isPlayerAmountSet = false
      isLastPlayerValid = true
      playerAmount = 0

    override def builder: MatchBuilder = matchBuilder

  def apply(matchBuilder: MatchBuilder): ControllerMatchInit = ControllerMatchInitImpl(matchBuilder)