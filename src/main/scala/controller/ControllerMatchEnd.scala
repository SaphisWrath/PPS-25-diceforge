package controller

import model.Players.Player
import model.resource.GloryPoint

trait ControllerMatchEnd:
  def getSortedPlayers: List[(Player, GloryPoint)]

class ControllerMatchEndImpl(playersWithPoints: List[(Player, GloryPoint)]) extends ControllerMatchEnd:
  override def getSortedPlayers: List[(Player, GloryPoint)] =
    playersWithPoints.sortBy(pair => - pair._2.amount)