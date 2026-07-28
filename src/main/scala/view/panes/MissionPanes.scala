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
  private class MissionPane(missionDTO: MissionDTO) extends ViewComponent:
    override def component: Node =
      new VBox {
        alignment = Center
        padding = Insets(10)
        children = Seq(
          TextFactory.makeMissionName(missionDTO.id),
          new EffectWrapperPane(LanguageStrings.MissionPaneStrings.cost, missionDTO.cost).component,
          new EffectWrapperPane(LanguageStrings.MissionPaneStrings.reward, missionDTO.rewards).component,
          ButtonFactory.makeBoardButton(LanguageStrings.MissionPaneStrings.get, ActionEvent => {})
        )
      }

  class MissionCell(missions: List[MissionDTO], vertical: Boolean = false) extends ViewComponent:
    private val gridPane = new GridPane()
    gridPane.border = new Border(new BorderStroke(
      Color.Green,
      BorderStrokeStyle.Solid,
      CornerRadii.Empty,
      BorderWidths.Default)
    )
    missions.zipWithIndex.foreach((m, i) => {
      gridPane.add(new MissionPane(m).component, i, 0)
    })
    override def component: Node = gridPane

  class MissionBoardPane(missions: Map[Int, List[MissionDTO]]) extends ViewComponent:
    private val borderPane = new BorderPane {
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
          MissionCell(missions(0)).component,
          MissionCell(missions(1)).component,
          MissionCell(missions(2)).component,
        )
      }
      bottom = new HBox {
        alignment = Center
        children = Seq(
          MissionCell(missions(3)).component,
          MissionCell(missions(4)).component,
          MissionCell(missions(5)).component,
          MissionCell(missions(6), true).component
        )
      }
    }
    override def component: Node = borderPane
    


