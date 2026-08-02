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
import view.text.TextFactory
import view.theme.JfxTheme
import view.utils.ViewUtils
import view.utils.ViewUtils.{makeBackgroundFill, makeBorder}
import controller.dto.MissionType.*

object MissionPanes:
  class MissionPane(missionDTO: MissionDTO) extends VBox:
    private val fillColor: Color = missionDTO.missionType match
      case Support => JfxTheme.tertiaryContainer
      case _ => JfxTheme.primaryContainer
      
    private val borderColor: Color = missionDTO.missionType match
      case Support => JfxTheme.tertiaryBorder
      case _ => JfxTheme.primaryBorder

    border = makeBorder(borderColor)
    background = makeBackgroundFill(fillColor)
    alignment = Center
    padding = Insets(10)
    spacing = 10
    children = Seq(
      name,
      cost,
      rewards,
      button
    )

    protected def button: Button =
      ButtonFactory.makeBoardButton(LanguageStrings.MissionPaneStrings.get, missionDTO.onClick, missionDTO.clickable)

    protected def rewards =
      new EffectWrapperPane(LanguageStrings.MissionPaneStrings.reward, missionDTO.rewards, borderColor)

    protected def cost =
      new EffectWrapperPane(LanguageStrings.MissionPaneStrings.cost, missionDTO.cost, borderColor)

    protected def name: Text = TextFactory.makeMissionName(missionDTO.id)
  
  class ObtainedMissionPane(missionDTO: MissionDTO) extends MissionPane(missionDTO):
    override protected def button: Button = ButtonFactory.makeBoardButton(
      LanguageStrings.MissionPaneStrings.supportGet,
      missionDTO.onClick,
      missionDTO.clickable
    )
  
  private class MissionCell(missions: Seq[MissionDTO], vertical: Boolean = false) extends HBox:
    border = makeBorder(JfxTheme.primaryBorder)
    padding = Insets(15)
    spacing = 10
    children = missions.map(m => MissionPane(m))

  class MissionBoardPane(missions: Map[Int, Seq[MissionDTO]]) extends BorderPane:
    private val contentSpacing: Double = 20
    padding = Insets(20)
    top = new HBox {
      alignment = Center
      spacing = contentSpacing
      children = Seq(
        MissionCell(missions(0)),
        MissionCell(missions(1)),
        MissionCell(missions(2)),
      )
    }
    bottom = new HBox {
      spacing = contentSpacing
      alignment = Center
      children = Seq(
        MissionCell(missions(3)),
        MissionCell(missions(4)),
        MissionCell(missions(5)),
        MissionCell(missions(6), true)
      )
    }
    


