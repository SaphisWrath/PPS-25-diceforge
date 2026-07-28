package view.panes

import controller.dto.EffectDTO
import scalafx.geometry.Pos.{Center, CenterLeft}
import scalafx.scene.Node
import scalafx.scene.layout.{Border, BorderStroke, BorderStrokeStyle, BorderWidths, CornerRadii, GridPane, VBox}
import scalafx.scene.paint.Color
import view.buttons.ButtonFactory
import view.scenes.ViewComponent
import view.text.TextFactory

object EffectPanes:
  private class EffectPane(effectDTO: EffectDTO) extends ViewComponent:
    override def component: Node = new VBox {
      alignment = Center
      width <= height
      border = new Border(new BorderStroke(
        Color.Red,
        BorderStrokeStyle.Solid,
        CornerRadii.Empty,
        BorderWidths.Default)
      )
      children = TextFactory.makeEffectText(effectDTO.toString)
    }
  
  class EffectWrapperPane(title: String, effectDTOs: List[EffectDTO]) extends ViewComponent:
    override def component: Node = new VBox {
      val gridPane = new GridPane()
      gridPane.maxWidth = 5
      effectDTOs.zipWithIndex.foreach((e, i)=>
        gridPane.add(
          EffectPane(e).component,
          0,
          i
        )
      )
      border = new Border(new BorderStroke(
        Color.Blue,
        BorderStrokeStyle.Solid,
        CornerRadii.Empty,
        BorderWidths.Default)
      )
      alignment = CenterLeft
      children = Seq(
        TextFactory.makeMissionLabel(title),
        gridPane,
      )
    }
