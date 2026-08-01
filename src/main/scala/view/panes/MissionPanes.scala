package view.panes

import controller.dto.MissionDTO
import scalafx.geometry.Insets
import scalafx.geometry.Pos.Center
import scalafx.scene.Node
import scalafx.scene.control.Button
import scalafx.scene.layout.*
import scalafx.scene.paint.Color
import scalafx.scene.text.Text
import view.LanguageStrings
import view.buttons.ButtonFactory
import view.panes.EffectPanes.EffectWrapperPane
import view.scenes.ViewComponent
import view.text.TextFactory

object MissionPanes:
  class MissionPane(missionDTO: MissionDTO) extends VBox:
    alignment = Center
    padding = Insets(10)
    children = Seq(
      name,
      cost.component,
      rewards.component,
      button
    )

    protected def button: Button =
      ButtonFactory.makeBoardButton(LanguageStrings.MissionPaneStrings.get, missionDTO.onClick, missionDTO.clickable)

    protected def rewards: ViewComponent =
      new EffectWrapperPane(LanguageStrings.MissionPaneStrings.reward, missionDTO.rewards)

    protected def cost: ViewComponent =
      new EffectWrapperPane(LanguageStrings.MissionPaneStrings.cost, missionDTO.cost)

    protected def name: Text = TextFactory.makeMissionName(missionDTO.id)
  
  class ObtainedMissionPane(missionDTO: MissionDTO) extends MissionPane(missionDTO):
    override protected def button: Button = ButtonFactory.makeBoardButton("BUY", missionDTO.onClick, missionDTO.clickable)
  
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
    


