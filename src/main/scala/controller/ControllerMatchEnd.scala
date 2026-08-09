package controller

import controller.dto.PlayerDTO
import controller.dto.pathfinders.ImagePathFinders.{findImagePath, given}
import model.GameMatch
import model.resource.{GloryPoint, Resource}

trait ControllerMatchEnd:
  def gameMatch_=(controller: GameMatch): Unit

  def sortedPlayers: Seq[(PlayerDTO, Int)]

  def gloryPointPath: String

object ControllerMatchEnd:
  private class ControllerMatchEndImpl(var gameMatch: GameMatch) extends ControllerMatchEnd:
    override def sortedPlayers: Seq[(PlayerDTO, Int)] =
      gameMatch.players
        .map(player => (player, player.board.gloryPoints.amount))
        .sortBy(pair => -pair._2)
        .map(pair => (PlayerDTO(pair._1), pair._2))

    override def gloryPointPath: String = findImagePath[Resource](GloryPoint(0))

  def apply(gameMatch: GameMatch): ControllerMatchEnd = ControllerMatchEndImpl(gameMatch)