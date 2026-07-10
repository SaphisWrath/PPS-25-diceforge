package view.buttons

import javafx.event.{ActionEvent, EventHandler}
import scalafx.beans.property.ObjectProperty
import scalafx.scene.control.Button
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{Border, BorderStroke, BorderStrokeStyle, BorderWidths, CornerRadii}
import scalafx.scene.paint.Color

trait ButtonFactory:
  def makeMenuButton(
                      test: String,
                      onClick: ObjectProperty[EventHandler[ActionEvent]]
                    ): Button
  def makeBoardButton(
                     buttonText: String,
                     onClick: ActionEvent => Unit
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

    override def makeBoardButton(buttonText: String, onClick: ActionEvent => Unit): Button =
      new Button {
        text = buttonText
        onAction = event => onClick(event)
      }

  def apply(): ButtonFactory = new ButtonFactoryImpl;
