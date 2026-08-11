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

  /**
   * @param color the color of the background
   * @param cornerRadii the cornerRadii for the border
   * @return a ScalaFX Background which is a solid color
   */
  def makeBackgroundFill(color: Color, cornerRadii: CornerRadii = CornerRadii(globalCornerRadii)): Background =
    Background(Array(BackgroundFill(color, cornerRadii, Insets.Empty)))


  /**
   * @param color the color of the background
   * @param cornerRadii the cornerRadii for the border
   * @param borderWidths the BorderWidths of the border
   * @return a ScalaFX Border with the requested parameters
   */
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