package view.panes

import controller.ViewPublisher
import controller.ViewPublisher.ViewSubscriber
import controller.dto.EffectDTO
import scalafx.scene.layout.HBox
import scalafx.scene.paint.Color
import view.panes.EffectPanes.EffectWrapperPane
import controller.ViewPublisher.ViewContext.*

object DicePanes:
  class DicePane(effects: () => Seq[Option[EffectDTO]], colorHex: String) extends HBox with ViewSubscriber:
    private def updateChildren(): Unit =
      if !effects().contains(None) then
        children = EffectWrapperPane("", effects().map(e => e.get), Color.valueOf(colorHex))
    updateChildren()

    override def update(context: ViewPublisher.ViewContext): Unit = context match
      case DiceThrownContext | TurnChangeContext => updateChildren()
      case _ =>
