package controller

import controller.dto.PlayerDTO
import controller.dto.pathfinders.ImagePathFinders.{findImagePath, given}
import model.GameMatch
import model.resource.{GloryPoint, Resource}

trait ControllerMatchEnd:
  /**
   * Sets the GameMatch this controller will use to determine the winner
   * @param gameMatch the ended match
   */
  def gameMatch_=(gameMatch: GameMatch): Unit

  /**
   *
   * @return the players sorted by best score, with said score attached
   */
  def sortedPlayers: Seq[(PlayerDTO, Int)]

  /**
   *
   * @return the image path for the GloryPoint resource
   */
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