package view.panes

import controller.dto.EffectDTO
import scalafx.geometry.Insets
import scalafx.geometry.Pos.{Center, CenterLeft}
import scalafx.scene.layout.*
import scalafx.scene.paint.Color
import view.sprites.Sprite
import view.text.TextFactory
import view.utils.ViewUtils.makeBorder

object EffectPanes:
  class EffectPane(effectDTO: EffectDTO, spriteDim: Option[Double] = None) extends StackPane:
    private val sprite = Sprite(effectDTO.sprite).getSpriteAsImageView
    spriteDim match
      case Some(dim) =>
        sprite.fitWidth = dim
        sprite.fitHeight = dim
      case _ =>

    alignment = Center
    width <= height
    children = sprite
    effectDTO.label match
      case Some(s) => children ++= Seq(TextFactory.makeEffectText(s))
      case _ =>

  class EffectWrapperPane(
                           title: String,
                           effectDTOs: Seq[EffectDTO],
                           color: Color,
                           spriteDim: Option[Double] = None) extends VBox:
    private val gridPane = new GridPane()
    effectDTOs.zipWithIndex.foreach((e, i) =>
      gridPane.add(
        EffectPane(e, spriteDim),
        i % 2,
        i / 2
      )
    )
    border = makeBorder(color)
    padding = Insets(10)
    alignment = CenterLeft
    children = Seq(
      TextFactory.makeMissionLabel(title),
      gridPane,
    )
