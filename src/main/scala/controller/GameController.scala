package controller

import model.*
import model.Players.Color

/**
 * A controller to handle communication with view about match initialization
 */
trait ControllerMatchInit:
  def setPlayerAmount(amount: Int): Unit
  def isPlayerAmountSet: Boolean
  def updateMatchInfo(name: String, color: String): Unit
  def isLastPlayerValid: Boolean
  def allPlayersSet: Boolean

class ControllerMatchInitImpl(playerFactory: PlayerFactory) extends ControllerMatchInit:
  var isPlayerAmountSet = false
  var isLastPlayerValid = true
  private var matchBuilder: MatchBuilder = MatchBuilderImpl(2)

  def setPlayerAmount(amount: Int): Unit =
    matchBuilder = MatchBuilderImpl(amount)
    isPlayerAmountSet = true

  def updateMatchInfo(name: String, color: String): Unit =
    if Color.stringToColor(color).isDefined
    then
      val nextPlayer = playerFactory.create(name, Color.stringToColor(color).get)
      isLastPlayerValid = nextPlayer.isDefined
      if isLastPlayerValid
      then matchBuilder = matchBuilder.addPlayer(nextPlayer.get)

  def allPlayersSet: Boolean = matchBuilder.ready