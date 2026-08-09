package view.panes

import controller.dto.DieDTO
import scalafx.scene.layout.GridPane
import view.panes.EffectPanes.EffectWrapperPane
import view.theme.JfxTheme

object DiePanes:
  class DiePane(dieDTO: DieDTO) extends GridPane:
    dieDTO.faces
      .map(EffectWrapperPane("", _, JfxTheme.primaryBorder))
      .zipWithIndex
      .foreach((effectPane, i) =>
        this.add(
          effectPane,
          i % 3,
          i / 2
        )
      )