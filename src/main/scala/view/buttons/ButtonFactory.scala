package view.buttons

import javafx.event.{ActionEvent, EventHandler}
import scalafx.beans.property.ObjectProperty
import scalafx.scene.control.Button
import scalafx.scene.layout.{Border, BorderStroke, BorderStrokeStyle, BorderWidths, CornerRadii}
import scalafx.scene.paint.Color
  
object ButtonFactory:
  def makeMenuButton(
                               text: String,
                               onClick: ActionEvent => Unit
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
      onAction = event => onClick(event)
    }
