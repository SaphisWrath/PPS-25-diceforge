package view

import scalafx.geometry.Insets
import scalafx.geometry.Pos.Center
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{Border, BorderPane, BorderStroke, BorderStrokeStyle, BorderWidths, CornerRadii, VBox}
import scalafx.scene.paint.Color.{DarkRed, Red}
import scalafx.scene.paint.{Color, LinearGradient, Paint, Stops}
import scalafx.scene.text.Text

class MainMenuScene extends Scene {
  root = new VBox {
    fillWidth = true
    spacing = 20
    border = new Border(new BorderStroke(
      Color.Black,
      BorderStrokeStyle.Solid,
      CornerRadii.Empty,
      BorderWidths.Default)
    )
    alignment = Center
    alignmentInParent = Center
    children = Seq(
      new Text {
        text = "DICE FORGE"
        style = "-fx-font: normal bold 50pt sans-serif"
        padding = Insets(10)
        fill = new LinearGradient(
          endX = 0,
          stops = Stops(Red, DarkRed))
      },
      new Button ("INIZIA") {
        minWidth = 100
        minHeight = 50
      },
      new Button ("REGOLE") {
        minWidth = 100
        minHeight = 50
      }
    )
  }
}
