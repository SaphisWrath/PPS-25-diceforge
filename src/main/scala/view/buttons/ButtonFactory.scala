package view.buttons

import scalafx.scene.control.Button
import scalafx.scene.layout.{Border, BorderStroke, BorderStrokeStyle, BorderWidths, CornerRadii}
import scalafx.scene.paint.Color

trait ButtonFactory:
  def makeMenuButton(test: String): Button
  
object ButtonFactory:
  private class ButtonFactoryImpl extends ButtonFactory:
    override def makeMenuButton(text: String): Button =
      new Button(text) {
        minWidth = 100
        minHeight = 50
        new Border(new BorderStroke(
          Color.Black,
          BorderStrokeStyle.Solid,
          CornerRadii(5),
          BorderWidths.Default)
        )
      }
  
  def apply(): ButtonFactory = new ButtonFactoryImpl;
