package view.panes

import controller.dto.{CompoundEffectDTO, EffectDTO}
import scalafx.geometry.Insets
import scalafx.geometry.Pos.{Center, CenterLeft}
import scalafx.scene.Node
import scalafx.scene.layout.*
import scalafx.scene.paint.Color
import view.sprites.Sprite
import view.text.TextFactory
import view.theme.JfxTheme
import view.utils.ViewUtils.makeBorder

import scala.annotation.tailrec

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

  class CompoundEffectPane(compoundEffectDTO: CompoundEffectDTO, color: Color) extends StackPane:
    children ++= Seq(
      EffectGridPane(compoundEffectDTO.effects, color)
    )
    border = makeBorder(color)
    compoundEffectDTO.label match
      case Some(s) => children ++= Seq(TextFactory.makeCompoundEffectText(s))
      case _ =>
  
  private class EffectGridPane(
                                effectDTOs: Seq[EffectDTO],
                                color: Color,
                                spriteDim: Option[Double] = None) extends GridPane:
    effectDTOs.zipWithIndex.foreach((e, i) =>
      add(
        e match
          case c: CompoundEffectDTO =>  CompoundEffectPane(c, color)
          case _ => EffectPane(e, spriteDim),
        i % 2,
        i / 2
      )
    )

  class EffectWrapperPane(title: String,
                          effectDTOs: Seq[EffectDTO],
                          color: Color) extends VBox:
    border = makeBorder(color)
    padding = Insets(10)
    alignment = CenterLeft
    children = Seq(
      TextFactory.makeMissionLabel(title),
      EffectGridPane(effectDTOs, color),
    )

  def effectPane(effectDTO: EffectDTO): StackPane = effectDTO match
    case e: CompoundEffectDTO => CompoundEffectPane(e, JfxTheme.primaryBorder)
    case e: EffectDTO => EffectPane(e)
