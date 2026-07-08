package view.buttons

import javafx.event.{ActionEvent, EventHandler}
import scalafx.beans.property.ObjectProperty
import scalafx.scene.control.Button
import scalafx.scene.layout.{Border, BorderStroke, BorderStrokeStyle, BorderWidths, CornerRadii}
import scalafx.scene.paint.Color

trait ButtonFactory:
  def makeMenuButton(
                      test: String,
                      onClick: ObjectProperty[EventHandler[ActionEvent]]
                    ): Button
  
object ButtonFactory:
  private class ButtonFactoryImpl extends ButtonFactory:
    override def makeMenuButton(
                                 text: String,
                                 onClick: ObjectProperty[EventHandler[ActionEvent]]
                               ): Button =
      new Button(text) {
        minWidth = 100
        minHeight = 50
        new Border(new BorderStroke(
          Color.Black,
          BorderStrokeStyle.Solid,
          CornerRadii.Empty,
          BorderWidths.Default)
        )
        onAction = onClick.value
      }
  
  def apply(): ButtonFactory = new ButtonFactoryImpl;
