package view.text

import scalafx.geometry.Insets
import scalafx.scene.paint.Color.{Black, Cyan, DarkRed, Peru, Red, White}
import scalafx.scene.paint.{LinearGradient, Paint, Stops}
import scalafx.scene.text.Text
import view.LanguageStrings.TitleScreenStrings as TSStrings

object TextFactory:
  def makeMenuTitle: Text =
    new Text {
      text = TSStrings.title
      style = "-fx-font: normal bold 50pt sans-serif"
      margin = Insets(10)
      fill = new LinearGradient(
        endX = 0,
        stops = Stops(Red, DarkRed))
    }
  
  def makeMissionName(name: String): Text =
    new Text {
      text = name
      style = "-fx-font: normal bold 15pt sans-serif"
    }
    
  def makeMissionLabel(label: String): Text = 
    new Text {
      text = label
    }
    
  def makeEffectText(label: String): Text =
    new Text {
      text = label
      style = "-fx-font: normal bolder 25pt sans-serif"
      fill = White
      stroke = Black
    }
