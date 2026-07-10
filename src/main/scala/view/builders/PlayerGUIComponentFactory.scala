package view.builders

import scalafx.scene.Node
import scalafx.scene.paint.Color
import view.builders.PlayerBoxes.*

case class PlayerGUIComponentFactory(playerName: String, playerColorHex: String):
  private val playerColor = Color.valueOf(playerColorHex)
  val resorceSeq: Seq[String] = Seq(
    "Oro",
    "Cristalli Solari",
    "Cristalli Lunari",
    "Punti Gloria"
  )

  def activePlayerBox: Node =
    PlayerBoxBuilder(PlayerBoxStyle.Standard)
      .withNameSection(playerName)
      .withCircleTokenSection(playerColor, 25)
      .withResourceSection(playerName, resorceSeq.map((_, 0)))
      .build

  def nonActivePlayerBox: Node =
    PlayerBoxBuilder(PlayerBoxStyle.Small)
      .withNameSection(playerName)
      .withCircleTokenSection(playerColor, 10)
      .withResourceSection(playerName, resorceSeq.map((_, 0)))
      .build