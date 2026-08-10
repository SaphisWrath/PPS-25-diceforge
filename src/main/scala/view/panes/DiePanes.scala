package view.panes

import controller.dto.{CompoundEffectDTO, DieDTO, EffectDTO}
import scalafx.scene.layout.GridPane
import view.panes.EffectPanes.{CompoundEffectPane, EffectPane, EffectWrapperPane, effectPane}
import view.theme.JfxTheme

object DiePanes:
  class DiePane(dieDTO: DieDTO) extends GridPane:
    dieDTO.faces
      .map(effectPane)
      .zipWithIndex
      .foreach((effectPane, i) =>
        this.add(
          effectPane,
          i % 3,
          i / 3
        )
      )