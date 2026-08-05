package view.panes

import controller.dto.EffectDTO
import scalafx.scene.layout.{HBox, StackPane}
import scalafx.scene.paint.Color
import view.panes.EffectPanes.EffectWrapperPane
import view.utils.ViewUtils.{makeBackgroundFill, makeBorder}

object DieFacePanes:
  class DieFacePane(borderColor: Color, bgColor: Color, effectDTO: Seq[EffectDTO]) extends StackPane:
    border = makeBorder(borderColor)
    background = makeBackgroundFill(bgColor)
    if effectDTO.length == 1 then
      children = EffectPanes.EffectPane(effectDTO.head)
    else
      children = EffectWrapperPane("", effectDTO, Color.Transparent)