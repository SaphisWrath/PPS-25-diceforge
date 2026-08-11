package view.buttons

import controller.publishers.ViewPublisher.{ViewContext, ViewSubscriber}
import controller.publishers.ViewPublisher.ViewContext.*
import controller.publishers.ViewPublisher
import javafx.event.{ActionEvent, EventHandler}
import scalafx.scene.Node
import scalafx.scene.control.Button
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.*
import scalafx.scene.paint.Color
import view.theme.JfxTheme
import view.utils.ViewUtils

object ButtonFactory:
  def makeMenuButton(
                      text: String,
                      onClick: ActionEvent => Unit
                    ): Button =
    new Button(text) {
      minWidth = 100
      minHeight = 50
      onAction = event => onClick(event)
      textFill = JfxTheme.onPrimaryContainer
      background = ViewUtils.makeBackgroundFill(JfxTheme.primaryContainer, CornerRadii(10))
      border = ViewUtils.makeBorder(JfxTheme.primaryBorder, CornerRadii(10))
    }

  def makeBoardButton(buttonText: String, onClick: () => Unit, isDisabled: () => Boolean = () => false): Button =
    val cornerRadii = CornerRadii(5)
    class ButtonSubscriber extends Button with ViewSubscriber:
      private def calculateColor(): Unit =
        textFill = if disabled.value then JfxTheme.onSecondaryContainer else JfxTheme.onTertiaryContainer
        background = ViewUtils.makeBackgroundFill(
          if disabled.value then JfxTheme.secondaryContainer else JfxTheme.tertiaryContainer,
          cornerRadii
        )
        border = ViewUtils.makeBorder(
          if disabled.value then JfxTheme.secondaryBorder else JfxTheme.tertiaryBorder,
          cornerRadii
        )

      text = buttonText
      onAction = event => onClick()
      disable = isDisabled()
      calculateColor()
      disable.onChange((_, _, newValue) => calculateColor())

      override def update(context: ViewContext): Unit = context match
        case _ => this.disable = isDisabled()

    val button = ButtonSubscriber()
    button.subscribeTo(ViewPublisher())
    button
    
  def makeChoiceButton(icon: Node, onClick: () => Unit): Button =
    new Button {
      graphic = icon
      onAction = event => onClick()
    }