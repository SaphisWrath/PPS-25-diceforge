package view.text

import scalafx.geometry.Insets
import scalafx.scene.paint.Color.{DarkRed, Red}
import scalafx.scene.paint.{LinearGradient, Stops}
import scalafx.scene.text.Text

trait TextFactory:
  def makeMenuTitle: Text
  
object TextFactory:
  private class TextFactoryImpl extends TextFactory {
    override def makeMenuTitle: Text =
      new Text {
        text = "DICE FORGE"
        style = "-fx-font: normal bold 50pt sans-serif"
        margin = Insets(10)
        fill = new LinearGradient(
          endX = 0,
          stops = Stops(Red, DarkRed))
      }
  }
  def apply(): TextFactory = new TextFactoryImpl
