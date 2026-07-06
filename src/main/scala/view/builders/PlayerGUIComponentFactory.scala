package view.builders

import scalafx.scene.paint.Color
import view.builders.PlayerBoxes.*

case class PlayerGUIComponentFactory(playerName: String, playerColor: Color):
  val resorceSeq: Seq[String] = Seq(
    "Oro",
    "Cristalli Solari",
    "Cristalli Lunari",
    "Punti Gloria"
  )

  def activePlayerBox: PlayerBox =
    new BasePlayerBox(PlayerBoxStyle.Standard)
      with StandardNameSection(playerName)
      with CircleTokenSection(25, playerColor)
      with StandardResourceSection(resorceSeq) //TODO

  def nonActivePlayerBox: PlayerBox =
    new BasePlayerBox(PlayerBoxStyle.Small)
      with StandardNameSection(playerName)
      with StandardResourceSection(resorceSeq) //TODO