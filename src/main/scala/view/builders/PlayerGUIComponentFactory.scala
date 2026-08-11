package view.builders

import controller.dto.{EffectDTO, PlayerBoardDTO, PlayerDTO}
import scalafx.scene.Node
import scalafx.scene.paint.Color
import view.builders.PlayerBoxes.*

case class PlayerGUIComponentFactory(
                                      playerName: String,
                                      playerColorHex: String,
                                      resourceProducers: Map[String, () => Int],
                                      resourceCapProducers: Map[String, () => Int],
                                      recentRollsProducer: () => Seq[Option[EffectDTO]]
                                    ):
  private val playerColor = Color.valueOf(playerColorHex)

  def activePlayerBox: Node =
    PlayerBoxBuilder(PlayerBoxStyle.Standard)
      .withBorderContainer
      .withNameSection(playerName)
      .withCircleTokenSection(playerColor, 25)
      .withResourceSection(resourceProducers, resourceCapProducers)
      .withDiceSection(recentRollsProducer, playerColorHex)
      .build

  def nonActivePlayerBox: Node =
    PlayerBoxBuilder(PlayerBoxStyle.Small)
      .withBorderContainer
      .withNameSection(playerName)
      .withCircleTokenSection(playerColor, 10)
      .withResourceSection(resourceProducers, resourceCapProducers)
      .withDiceSection(recentRollsProducer, playerColorHex)
      .build

  def onlyToken: Node = PlayerBoxBuilder(PlayerBoxStyle.None)
    .withStackContainer
    .withCircleTokenSection(playerColor, 8)
    .build

object PlayerGUIComponentFactory:
  def apply(player: PlayerDTO, playerBoard: PlayerBoardDTO, rollsProducer: () => Seq[Option[EffectDTO]]): PlayerGUIComponentFactory =
    PlayerGUIComponentFactory(
      player.name,
      player.colorHex,
      playerBoard.resourceList.map(r => (r, () => playerBoard.amountOf(r))).toMap,
      playerBoard.resourceList
        .filter(r => playerBoard.capOf(r).nonEmpty)
        .map(r => (r, () => playerBoard.capOf(r).get))
        .toMap,
      rollsProducer
    )