package controller.dto

import controller.converters.ColorConverter.*
import model.Players.Player

case class PlayerDTO(name: String, colorHex: String)

object PlayerDTO:
  def apply(player: Player): PlayerDTO = PlayerDTO(player.getName, player.getColor.toHex)

  extension (playerDTO: PlayerDTO)
    def toPlayer: Player = Player(playerDTO.name, playerDTO.colorHex.toColor)
  val empty: PlayerDTO = PlayerDTO("", "")
