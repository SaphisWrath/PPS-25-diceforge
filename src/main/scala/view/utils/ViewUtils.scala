package view.utils

import javafx.stage.Screen
import scalafx.geometry.Insets
import scalafx.scene.layout.*
import scalafx.scene.paint.Color

object ViewUtils:
  private val globalCornerRadii = 15
  private val globalBorderWidth = 4
  val screenWidth: Double = Screen.getPrimary.getVisualBounds.getWidth
  val screenHeight: Double = Screen.getPrimary.getVisualBounds.getHeight

  def makeBackgroundFill(color: Color, cornerRadii: CornerRadii = CornerRadii(globalCornerRadii)): Background =
    Background(Array(BackgroundFill(color, cornerRadii, Insets.Empty)))

  def makeBorder(
                  color: Color,
                  cornerRadii: CornerRadii = CornerRadii(globalCornerRadii),
                  borderWidths: BorderWidths = BorderWidths(globalBorderWidth)
                ): Border =
    new Border(new BorderStroke(
      color,
      BorderStrokeStyle.Solid,
      cornerRadii,
      borderWidths)
    )