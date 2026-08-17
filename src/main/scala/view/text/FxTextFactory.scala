package view.text

import scalafx.geometry.Insets
import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.paint.Color.{Black, Cyan, DarkRed, Peru, Red, White}
import scalafx.scene.paint.{Color, LinearGradient, Paint, Stops}
import scalafx.scene.text.{Font, Text}
import scalafx.stage.Popup
import view.LanguageStrings.TitleScreenStrings as TSStrings
import view.theme.JfxTheme

object FxTextFactory extends TextFactory[Text, Label]:
  override def makeMenuTitle: Text =
    new Text {
      text = TSStrings.title
      style = "-fx-font: normal bold 75pt sans-serif"
      margin = Insets(10)
      fill = new LinearGradient(
        endX = 0,
        stops = Stops(JfxTheme.primary, JfxTheme.primaryBorder))
      stroke = JfxTheme.primaryBorder
    }

  override def makeRulesLabel(text: String, parentWidth: Double, parentHeight: Double): Label =
    new Label(text) {
      font = Font.font(20)
      wrapText = true
      prefWidth = parentWidth - 20
      maxWidth = parentWidth - 20
    }
  
  override def makeMissionName(name: String): Text =
    new Text {
      text = name
      style = "-fx-font: normal bold 12pt sans-serif"
      fill = JfxTheme.onPrimaryContainer
    }

  override def makeMissionLabel(label: String): Text =
    new Text {
      text = label
      style = "-fx-font: normal bold 10pt sans-serif"
      fill = JfxTheme.onPrimaryContainer
    }

  override def makeTurnCounterText(label: String): Text =
    new Text {
      text = label
      style = "-fx-font: normal bold 25pt sans-serif"
      stroke = Black
      fill = JfxTheme.primary
    }

  override def makeEffectText(label: String): Text = {
    val text = try
        label.toInt
        new Text {
          style = "-fx-font: normal bolder 20pt sans-serif"
        }
      catch
        case _ =>
            new Text {
              style = "-fx-font: normal bolder 15pt sans-serif"
            }
    text.fill = White
    text.stroke = Black
    text.text = label
    text
  }

  override def makeCompoundEffectText(label: String): Text =
    new Text {
      text = label
      style = "-fx-font: normal bolder 30pt sans-serif"
      fill = Color.White
      stroke = JfxTheme.primaryBorder
    }
