package view.builders

import controller.dto.{PlayerBoardDTO, PlayerDTO}
import scalafx.scene.Node
import scalafx.scene.paint.Color
import view.builders.PlayerBoxes.*

case class PlayerGUIComponentFactory(
                                      playerName: String,
                                      playerColorHex: String,
                                      resourceProducers: Map[String, () => Int],
                                      resourceCapProducers: Map[String, () => Int]
                                    ):
  private val playerColor = Color.valueOf(playerColorHex)

  def activePlayerBox: Node =
    PlayerBoxBuilder(PlayerBoxStyle.Standard)
      .withNameSection(playerName)
      .withCircleTokenSection(playerColor, 25)
      .withResourceSection(resourceProducers, resourceCapProducers)
      .build

  def nonActivePlayerBox: Node =
    PlayerBoxBuilder(PlayerBoxStyle.Small)
      .withNameSection(playerName)
      .withCircleTokenSection(playerColor, 10)
      .withResourceSection(resourceProducers, resourceCapProducers)
      .build

  def onlyToken: Node = circleTokenComponent(playerColor, 10)

object PlayerGUIComponentFactory:
  def apply(player: PlayerDTO, playerBoard: PlayerBoardDTO): PlayerGUIComponentFactory =
    PlayerGUIComponentFactory(
      player.name,
      player.colorHex,
      playerBoard.resourceList.map(r => (r, () => playerBoard.amountOf(r))).toMap,
      playerBoard.resourceList
        .filter(r => playerBoard.capOf(r).nonEmpty)
        .map(r => (r, () => playerBoard.capOf(r).get))
        .toMap
    )