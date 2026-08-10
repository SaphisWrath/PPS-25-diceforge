package view.buttons

import controller.ViewPublisher
import controller.ViewPublisher.{ViewContext, ViewSubscriber}
import controller.ViewPublisher.ViewContext.*
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
      border = ViewUtils.makeBorder(JfxTheme.onPrimaryContainer, CornerRadii(10))
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
          if disabled.value then JfxTheme.onSecondaryContainer else JfxTheme.onTertiaryContainer,
          cornerRadii
        )

      text = buttonText
      onAction = event => onClick()
      disable = isDisabled()
      calculateColor()
      disable.onChange((_, _, newValue) => calculateColor())

      override def update(context: ViewContext): Unit = context match
        case MissionBoughtContext | TurnChangeContext | ResourceContext | TurnStepChangeContext => this.disable = isDisabled()
        case _ =>

    val button = ButtonSubscriber()
    button.setPublisher(ViewPublisher())
    button
    
  def makeChoiceButton(icon: Node, onClick: () => Unit): Button =
    new Button {
      graphic = icon
      onAction = event => onClick()
    }