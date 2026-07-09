package controller.dto

import model.resource.*
import controller.converters.ResourceConverters.*

case class PlayerBoardDTO(resourceMap: Map[String, Int])

object PlayerBoardDTO:
  def apply(board: PlayerBoard): PlayerBoardDTO =
    PlayerBoardDTO(Map(
      resourceToString(board.gold) -> board.gold.amount,
      resourceToString(board.sunCrystals) -> board.sunCrystals.amount,
      resourceToString(board.moonCrystals) -> board.moonCrystals.amount,
      resourceToString(board.gloryPoints) -> board.gloryPoints.amount
    ))

