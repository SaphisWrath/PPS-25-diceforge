package view.builders

import scalafx.scene.Node
import scalafx.scene.paint.Color
import view.builders.PlayerBoxes.*

case class PlayerGUIComponentFactory(playerName: String, playerColor: Color):
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
      .withResourceSection(resorceSeq)
      .build

  def nonActivePlayerBox: Node =
    PlayerBoxBuilder(PlayerBoxStyle.Small)
      .withNameSection(playerName)
      .withCircleTokenSection(playerColor, 10)
      .withResourceSection(resorceSeq)
      .build