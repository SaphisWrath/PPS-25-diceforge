package controller

import controller.dto.PlayerDTO
import model.GameMatch

trait ControllerMatchEnd:
  def gameMatch_=(controller: GameMatch): Unit

  def getSortedPlayers: Seq[(PlayerDTO, Int)]

object ControllerMatchEnd:
  private class ControllerMatchEndImpl(var gameMatch: GameMatch) extends ControllerMatchEnd:
    override def getSortedPlayers: Seq[(PlayerDTO, Int)] =
      gameMatch.players
        .map(player => (player, player.board.gloryPoints.amount))
        .sortBy(pair => -pair._2)
        .map(pair => (PlayerDTO(pair._1), pair._2))

  def apply(gameMatch: GameMatch): ControllerMatchEnd = ControllerMatchEndImpl(gameMatch)