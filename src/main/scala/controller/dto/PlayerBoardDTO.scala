package controller.dto

import model.resource.*

case class PlayerBoardDTO(resourceMap: Map[String, Int])

object PlayerBoardDTO:
  def apply(board: PlayerBoard): PlayerBoardDTO =
    PlayerBoardDTO(Map(
      string(board.gold) -> board.gold.amount,
      string(board.sunCrystals) -> board.sunCrystals.amount,
      string(board.moonCrystals) -> board.moonCrystals.amount,
      string(board.gloryPoints) -> board.gloryPoints.amount
    ))

  private def string(resource: Resource): String = resource match
    case Gold(_) => "Oro"
    case SunCrystal(_) => "Cristalli Sola"
    case MoonCrystal(_) => "Cristalli Lunari"
    case GloryPoint(_) => "Punti Gloria"

