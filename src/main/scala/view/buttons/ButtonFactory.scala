package view.buttons

import controller.ViewPublishers
import controller.ViewPublishers.Context.{ActionContext, MissionBoughtContext, ResourceContext, TurnChangeContext}
import controller.ViewPublishers.{ViewPublisher, ViewSubscriber}
import javafx.event.{ActionEvent, EventHandler}
import scalafx.beans.property.ObjectProperty
import scalafx.scene.control.Button
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.*
import scalafx.scene.paint.Color

object ButtonFactory:
  def makeMenuButton(
                      text: String,
                      onClick: ActionEvent => Unit
                    ): Button =
    new Button(text) {
      minWidth = 100
      minHeight = 50
      border = new Border(new BorderStroke(
        Color.Black,
        BorderStrokeStyle.Solid,
        CornerRadii.Empty,
        BorderWidths.Default)
      )
      onAction = event => onClick(event)
    }

  def makeBoardButton(buttonText: String, onClick: () => Unit, isDisabled: () => Boolean = () => false): Button =
    class ButtonSubscriber extends Button with ViewSubscriber {
      text = buttonText
      onAction = event => onClick()
      disable = isDisabled()

      override def update(context: ViewPublishers.Context): Unit = context match
        case MissionBoughtContext | TurnChangeContext | ResourceContext | ActionContext => this.disable = isDisabled()
        case _ =>
    }

    val button = ButtonSubscriber()
    button.setPublisher(ViewPublisher)
    button