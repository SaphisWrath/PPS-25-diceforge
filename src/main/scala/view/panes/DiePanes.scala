package view.panes

import controller.dto.{CompoundEffectDTO, DieDTO, EffectDTO}
import javafx.geometry.VPos
import javafx.scene.layout.Priority
import scalafx.scene.layout.{GridPane, RowConstraints}
import view.panes.EffectPanes.{CompoundEffectPane, EffectPane, EffectWrapperPane, effectPane}
import view.theme.JfxTheme

object DiePanes:
  class DiePane(dieDTO: DieDTO) extends GridPane:
    dieDTO.faces
      .map(effectPane(_))
      .zipWithIndex
      .foreach((effectPane, i) =>
        this.add(
          effectPane,
          i % 3,
          i / 3
        )
      )