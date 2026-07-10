package controller.dto

import model.resource.*
import controller.converters.ResourceConverters.*

case class PlayerBoardDTO(resourceMap: Map[String, Int])

object PlayerBoardDTO:
  def apply(board: PlayerBoard): PlayerBoardDTO =
    PlayerBoardDTO(Map(
      resourceToString(board.gold.resource) -> board.gold.amount,
      resourceToString(board.sunCrystals.resource) -> board.sunCrystals.amount,
      resourceToString(board.moonCrystals.resource) -> board.moonCrystals.amount,
      resourceToString(board.gloryPoints) -> board.gloryPoints.amount
    ))

