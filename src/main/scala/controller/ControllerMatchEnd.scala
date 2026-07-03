package controller

import model.Players.Player
import model.resource.ResourceType.VictoryPoint
import model.resource.Resource

trait ControllerMatchEnd:
  def getSortedPlayers: List[(Player, Resource[VictoryPoint])]
  
class ControllerMatchEndImpl(playersWithPoints: List[(Player, Resource[VictoryPoint])]) extends ControllerMatchEnd:
  override def getSortedPlayers: List[(Player, Resource[VictoryPoint])] =
    playersWithPoints.sortBy(pair => - pair._2.currentAmount)