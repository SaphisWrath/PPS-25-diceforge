package view.panes

import controller.ViewPublisher
import controller.ViewPublisher.ViewContext.*
import controller.ViewPublisher.ViewSubscriber
import controller.dto.{DieDTO, EffectDTO}
import scalafx.scene.layout.{GridPane, HBox}
import scalafx.scene.paint.Color
import view.panes.EffectPanes.{EffectGridPane, effectPane}

object DicePanes:
  class FacePane(effects: () => Seq[Option[EffectDTO]], colorHex: String) extends HBox with ViewSubscriber:
    setPublisher(ViewPublisher())
    private def updateChildren(): Unit =
      if !effects().contains(None) then
        children = EffectGridPane(effects().map(e => e.get), Color.valueOf(colorHex), true)
    updateChildren()
    fillHeight = false

    override def update(context: ViewPublisher.ViewContext): Unit = context match
      case DiceThrownContext | TurnChangeContext => updateChildren()
      case _ =>

  class DiePane(dieDTO: DieDTO) extends GridPane:
    dieDTO.faces
      .map(effectPane(_))
      .zipWithIndex
      .foreach((effectPane, i) =>
        this.add(
          effectPane,
          i % 3,
          i / 3
        )
      )