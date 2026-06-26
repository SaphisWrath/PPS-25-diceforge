package controller

import model.*

trait ControllerMatchInit:
  def isPlayerAmountSet: Boolean
  def isLastPlayerValid: Boolean
  def setPlayerAmount(amount: Int): Unit
  def updateMatchInfo(name: String, color: String): Unit
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