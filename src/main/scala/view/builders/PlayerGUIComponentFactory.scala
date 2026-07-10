package view.builders

import controller.GameController
import controller.dto.{PlayerBoardDTO, PlayerDTO}
import scalafx.scene.Node
import scalafx.scene.paint.Color
import view.builders.PlayerBoxes.*

case class PlayerGUIComponentFactory(playerName: String, playerColorHex: String, resourceProducers: Map[String, () => Int]):
  private val playerColor = Color.valueOf(playerColorHex)

  def activePlayerBox: Node =
    PlayerBoxBuilder(PlayerBoxStyle.Standard)
      .withNameSection(playerName)
      .withCircleTokenSection(playerColor, 25)
      .withResourceSection(playerName, resourceProducers.toSeq)
      .build

  def nonActivePlayerBox: Node =
    PlayerBoxBuilder(PlayerBoxStyle.Small)
      .withNameSection(playerName)
      .withCircleTokenSection(playerColor, 10)
      .withResourceSection(playerName, resourceProducers.toSeq)
      .build

object PlayerGUIComponentFactory:
  def apply(player: PlayerDTO, playerBoard: PlayerBoardDTO): PlayerGUIComponentFactory =
    PlayerGUIComponentFactory(
      player.name,
      player.colorHex,
      playerBoard.resourceMap.map((name,_) => (name, () => GameController.playerBoard(player).get(name)))
    )