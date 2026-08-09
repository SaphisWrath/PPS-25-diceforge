package view.text

import scalafx.geometry.Insets
import scalafx.scene.Node
import scalafx.scene.paint.Color.{Black, Cyan, DarkRed, Peru, Red, White}
import scalafx.scene.paint.{Color, LinearGradient, Paint, Stops}
import scalafx.scene.text.Text
import scalafx.stage.Popup
import view.LanguageStrings.TitleScreenStrings as TSStrings
import view.theme.JfxTheme

object TextFactory:
  def makeMenuTitle: Text =
    new Text {
      text = TSStrings.title
      style = "-fx-font: normal bold 75pt sans-serif"
      margin = Insets(10)
      fill = new LinearGradient(
        endX = 0,
        stops = Stops(JfxTheme.primary, JfxTheme.tertiary))
      stroke = JfxTheme.tertiary
    }

  def makeMissionName(name: String): Text =
    new Text {
      text = name
      style = "-fx-font: normal bold 15pt sans-serif"
      fill = JfxTheme.onPrimaryContainer
    }

  def makeMissionLabel(label: String): Text =
    new Text {
      text = label
      style = "-fx-font: normal bold 10pt sans-serif"
      fill = JfxTheme.onPrimaryContainer
    }

  def makeEffectText(label: String): Text =
    new Text {
      text = label
      style = "-fx-font: normal bolder 20pt sans-serif"
      fill = White
      stroke = Black
    }

  def makeCompoundEffectText(label: String): Text =
    new Text {
      text = label
      style = "-fx-font: normal bolder 30pt sans-serif"
      fill = Color.White
      stroke = JfxTheme.primaryBorder
    }
