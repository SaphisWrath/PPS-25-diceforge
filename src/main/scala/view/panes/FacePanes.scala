package view.panes

import controller.dto.FaceDTO
import scalafx.geometry.Insets
import scalafx.geometry.Pos.Center
import scalafx.scene.Node
import scalafx.scene.layout.{Border, BorderStroke, BorderStrokeStyle, BorderWidths, CornerRadii, GridPane, HBox}
import scalafx.scene.paint.Color
import view.scenes.ViewComponent

class FacePane(faceDTO: FaceDTO) extends ViewComponent:
  override def component: Node = new HBox {
    alignment = Center
    padding = Insets(2)
    maxWidth = 70
    maxHeight = 70
    border = new Border(new BorderStroke(
      if faceDTO.choose then Color.Purple else Color.Brown,
      BorderStrokeStyle.Solid,
      CornerRadii.Empty,
      BorderWidths.Default)
    )
    children = EffectWrapperPane("",faceDTO.effects).component
  }
