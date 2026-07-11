package view.panes

import controller.dto.EffectDTO
import scalafx.geometry.Pos.{Center, CenterLeft}
import scalafx.scene.Node
import scalafx.scene.layout.{Border, BorderStroke, BorderStrokeStyle, BorderWidths, CornerRadii, GridPane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.shape.StrokeLineCap.Square
import view.buttons.ButtonFactory
import view.scenes.ViewComponent
import view.text.TextFactory

class EffectPane(effectDTO: EffectDTO) extends ViewComponent:
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
    effectDTOs.zipWithIndex.foreach((e, i)=>
      gridPane.add(
        EffectPane(e).component,
        i % 2,
        i / 2
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
      ButtonFactory.makeBoardButton("Prendi", ActionEvent => {})
    )
  }
