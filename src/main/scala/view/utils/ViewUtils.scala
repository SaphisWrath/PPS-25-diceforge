package view.utils

import scalafx.geometry.Insets
import scalafx.scene.layout.*
import scalafx.scene.paint.Color

object ViewUtils:
  val globalCornerRadii = 15
  val globalBorderWidth = 4

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