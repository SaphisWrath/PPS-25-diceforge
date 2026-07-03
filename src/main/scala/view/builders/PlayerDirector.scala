package view.builders

import scalafx.scene.paint.Color
import view.builders.PlayerBoxes.*

case class PlayerDirector(playerName: String, playerColor: Color):
  val resorceSeq: Seq[String] = Seq(
    "Oro",
    "Cristalli Solari",
    "Cristalli Lunari",
    "Punti Gloria"
  )

  def activePlayerBox: PlayerBox =
    new BasePlayerBox(playerName)
      with RoundedCorners(10)
      with SolidBorder(Color.Black, 3)
      with CircleTokenSection(25, playerColor)
      with StandardResourceSection(resorceSeq) //TODO