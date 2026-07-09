package controller.dto

import model.Players.Player
import controller.converters.ColorConverter.*

case class PlayerDTO(name: String, colorHex: String)

object PlayerDTO:
  def apply(player: Player): PlayerDTO = PlayerDTO(player.getName, player.getColor.toHex)
