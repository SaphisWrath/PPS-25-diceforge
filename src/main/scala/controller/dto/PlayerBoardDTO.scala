package controller.dto

import model.resource.*
import controller.converters.ResourceConverters.*

case class PlayerBoardDTO(resourceMap: Map[String, Int], resourceMaxMap: Map[String, Int])

object PlayerBoardDTO:
  def apply(board: PlayerBoard): PlayerBoardDTO =
    def extractResourcesAmounts(playerBoard: PlayerBoard): Map[String, Int] = Map(
        resourceToString(board.gold.resource) -> board.gold.amount,
        resourceToString(board.sunCrystals.resource) -> board.sunCrystals.amount,
        resourceToString(board.moonCrystals.resource) -> board.moonCrystals.amount,
        resourceToString(board.gloryPoints) -> board.gloryPoints.amount
      )
    def extractResourceMaximumAmounts(playerBoard: PlayerBoard): Map[String, Int] = Map(
        resourceToString(board.gold.resource) -> board.gold.maxCapacity,
        resourceToString(board.sunCrystals.resource) -> board.sunCrystals.maxCapacity,
        resourceToString(board.moonCrystals.resource) -> board.moonCrystals.maxCapacity
      )
    PlayerBoardDTO(
      extractResourcesAmounts(board),
      extractResourceMaximumAmounts(board)
    )

