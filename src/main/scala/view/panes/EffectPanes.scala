package view.panes

import controller.dto.EffectDTO
import scalafx.geometry.Insets
import scalafx.geometry.Pos.{Center, CenterLeft}
import scalafx.scene.layout.*
import view.sprites.Sprite
import view.text.TextFactory
import view.theme.JfxTheme
import view.utils.ViewUtils.makeBorder

object EffectPanes:
  private class EffectPane(effectDTO: EffectDTO) extends StackPane:
    alignment = Center
    width <= height
    children = Sprite(effectDTO.sprite).getSpriteAsImageView
    effectDTO.label match
      case Some(s) => children ++= Seq(TextFactory.makeEffectText(s))
      case _ =>

  class EffectWrapperPane(title: String, effectDTOs: Seq[EffectDTO]) extends VBox:
    private val gridPane = new GridPane()
    gridPane.maxWidth = 5
    effectDTOs.zipWithIndex.foreach((e, i) =>
      gridPane.add(
        EffectPane(e),
        i % 2,
        i / 2
      )
    )
    border = makeBorder(JfxTheme.tertiary)
    padding = Insets(10)
    alignment = CenterLeft
    children = Seq(
      TextFactory.makeMissionLabel(title),
      gridPane,
    )
