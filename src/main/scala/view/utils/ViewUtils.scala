package view.utils

import scalafx.geometry.Insets
import scalafx.scene.layout.*
import scalafx.scene.paint.Color

object ViewUtils:
  def makeBackgroundFill(color: Color, cornerRadii: CornerRadii = CornerRadii(5)): Background =
    Background(Array(BackgroundFill(color, cornerRadii, Insets.Empty)))

  def makeBorder(
                  color: Color,
                  cornerRadii: CornerRadii = CornerRadii(5),
                  borderWidths: BorderWidths = BorderWidths(2)
                ): Border =
    new Border(new BorderStroke(
      color,
      BorderStrokeStyle.Solid,
      cornerRadii,
      borderWidths)
    )