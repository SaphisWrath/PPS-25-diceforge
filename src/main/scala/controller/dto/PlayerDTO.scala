package controller.dto

import controller.converters.ColorConverter.*
import model.Players.Player

case class PlayerDTO(name: String, colorHex: String)

object PlayerDTO:
  def apply(player: Player): PlayerDTO = PlayerDTO(player.name, player.color.toHex)

  private val empty_name = ""
  private val empty_color_hex = ""
  val empty: PlayerDTO = PlayerDTO(empty_name, empty_color_hex)
