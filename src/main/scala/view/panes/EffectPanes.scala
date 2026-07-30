package view.panes

import controller.dto.EffectDTO
import scalafx.geometry.Pos.{Center, CenterLeft}
import scalafx.scene.Node
import scalafx.scene.layout.{Background, Border, BorderStroke, BorderStrokeStyle, BorderWidths, CornerRadii, GridPane, StackPane, VBox}
import scalafx.scene.paint.Color
import view.buttons.ButtonFactory
import view.scenes.ViewComponent
import view.sprites.Sprite
import view.text.TextFactory

object EffectPanes:
  private class EffectPane(effectDTO: EffectDTO) extends ViewComponent:
    override def component: Node = new StackPane {
      alignment = Center
      width <= height
      border = new Border(new BorderStroke(
        Color.Red,
        BorderStrokeStyle.Solid,
        CornerRadii.Empty,
        BorderWidths.Default)
      )
      children = Sprite(effectDTO.sprite).getSpriteAsImageView
      effectDTO.label match
        case Some(s) => children ++= Seq(TextFactory.makeEffectText(s))
        case _ => 
    }
  
  class EffectWrapperPane(title: String, effectDTOs: Seq[EffectDTO]) extends ViewComponent:
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
