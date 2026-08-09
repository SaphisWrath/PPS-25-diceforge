package view.panes

import controller.dto.{CompoundEffectDTO, EffectDTO}
import scalafx.geometry.Insets
import scalafx.geometry.Pos.{Center, CenterLeft}
import scalafx.scene.Node
import scalafx.scene.layout.*
import scalafx.scene.paint.Color
import view.sprites.Sprite
import view.text.TextFactory
import view.utils.ViewUtils.makeBorder

object EffectPanes:
  class EffectPane(effectDTO: EffectDTO) extends StackPane:
    alignment = Center
    width <= height
    children = Sprite(effectDTO.sprite).getSpriteAsImageView
    effectDTO.label match
      case Some(s) => children ++= Seq(TextFactory.makeEffectText(s))
      case _ =>

  trait EffectWrapperPane extends Node
  
  object EffectWrapperPane:
    private class EffectWrapperPaneImpl(title: String,
                                        effectDTOs: Seq[EffectDTO],
                                        color: Color,
                                        extraLabel: Option[String]) extends VBox with EffectWrapperPane:
      private val gridPane = new GridPane()
      gridPane.maxWidth = 5
      effectDTOs.zipWithIndex.foreach((e, i) =>
        gridPane.add(
          EffectPane(e),
          i % 2,
          i / 2
        )
      )
      border = makeBorder(color)
      padding = Insets(10)
      alignment = CenterLeft
      children = Seq(
        TextFactory.makeMissionLabel(title),
        if extraLabel.isDefined
        then
          new StackPane {
            children = Seq(gridPane, TextFactory.makeEffectText(extraLabel.get))
          }
        else gridPane
      )
      
    def apply(title: String, effectDTOs: Seq[EffectDTO], color: Color): EffectWrapperPane =
      EffectWrapperPaneImpl(title, effectDTOs, color, None)

    def apply(title: String, compoundEffectDTO: CompoundEffectDTO, color: Color): EffectWrapperPane =
      EffectWrapperPaneImpl(title, compoundEffectDTO.effects, color, compoundEffectDTO.label)