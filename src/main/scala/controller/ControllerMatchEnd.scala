package controller

import model.GameMatch
import model.Players.Player
import model.resource.GloryPoint

trait ControllerMatchEnd:
  def gameMatch_=(controller: GameMatch): Unit
  def getSortedPlayers: Seq[(Player, GloryPoint)]

object ControllerMatchEnd:
  private class ControllerMatchEndImpl(var gameMatch: GameMatch) extends ControllerMatchEnd:
    override def getSortedPlayers: Seq[(Player, GloryPoint)] =
      gameMatch.players
        .map(player => (player, gameMatch.playerBoardOf(player).gloryPoints.amount))
        .sortBy(pair => - pair._2)
        .map(pair => (pair._1, GloryPoint(pair._2)))
        
  def apply(gameMatch: GameMatch): ControllerMatchEnd = ControllerMatchEndImpl(gameMatch)