package view.panes

import controller.dto.MissionDTO
import scalafx.geometry.Insets
import scalafx.geometry.Pos.Center
import scalafx.scene.Node
import scalafx.scene.layout.*
import scalafx.scene.paint.Color
import view.LanguageStrings
import view.buttons.ButtonFactory
import view.panes.EffectPanes.EffectWrapperPane
import view.scenes.ViewComponent
import view.text.TextFactory

object MissionPanes:
  private class MissionPane(missionDTO: MissionDTO) extends VBox:
    alignment = Center
    padding = Insets(10)
    children = Seq(
      TextFactory.makeMissionName(missionDTO.id),
      new EffectWrapperPane(LanguageStrings.MissionPaneStrings.cost, missionDTO.cost).component,
      new EffectWrapperPane(LanguageStrings.MissionPaneStrings.reward, missionDTO.rewards).component,
      ButtonFactory.makeBoardButton(LanguageStrings.MissionPaneStrings.get, ActionEvent => {})
    )

  private class MissionCell(missions: Seq[MissionDTO], vertical: Boolean = false):
    val pane = new GridPane()
    pane.border = new Border(new BorderStroke(
      Color.Green,
      BorderStrokeStyle.Solid,
      CornerRadii.Empty,
      BorderWidths.Default)
    )
    missions.zipWithIndex.foreach((m, i) => {
      pane.add(new MissionPane(m), i, 0)
    })

  class MissionBoardPane(missions: Map[Int, Seq[MissionDTO]]):
    val pane = new BorderPane {
      padding = Insets(20)
      border = new Border(new BorderStroke(
        Color.Yellow,
        BorderStrokeStyle.Solid,
        CornerRadii.Empty,
        BorderWidths.Default)
      )
      top = new HBox {
        alignment = Center
        children = Seq(
          MissionCell(missions(0)).pane,
          MissionCell(missions(1)).pane,
          MissionCell(missions(2)).pane,
        )
      }
      bottom = new HBox {
        alignment = Center
        children = Seq(
          MissionCell(missions(3)).pane,
          MissionCell(missions(4)).pane,
          MissionCell(missions(5)).pane,
          MissionCell(missions(6), true).pane
        )
      }
    }
    


