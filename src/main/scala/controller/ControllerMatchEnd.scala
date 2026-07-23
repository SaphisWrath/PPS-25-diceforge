package controller

import controller.dto.PlayerDTO
import model.Players.Player
import model.resource.GloryPoint
import view.LanguageStrings.ResourceStrings

trait ControllerMatchEnd:
  def gameController_=(controller: GameController): Unit
  def getSortedPlayers: Seq[(Player, GloryPoint)]

object ControllerMatchEnd extends ControllerMatchEnd:
  var gameController: GameController = GameController

  override def getSortedPlayers: Seq[(Player, GloryPoint)] =
    gameController.players
      .map(player => (player.toPlayer, gameController.playerBoard(player).amountOf(ResourceStrings.gloryPoint)))
      .sortBy(pair => - pair._2)
      .map(pair => (pair._1, GloryPoint(pair._2)))