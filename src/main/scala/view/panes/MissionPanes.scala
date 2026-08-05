package view.panes

import controller.dto.MissionDTO
import scalafx.geometry.Insets
import scalafx.geometry.Pos.Center
import scalafx.scene.control.{Button, Tooltip}
import scalafx.scene.layout.*
import scalafx.scene.paint.Color
import scalafx.scene.text.Text
import view.LanguageStrings
import view.buttons.ButtonFactory
import view.panes.EffectPanes.EffectWrapperPane
import view.text.{MissionDescriptions, TextFactory}
import view.theme.JfxTheme
import view.utils.ViewUtils
import view.utils.ViewUtils.{makeBackgroundFill, makeBorder}
import controller.dto.MissionType.*

object MissionPanes:
  class MissionPane(missionDTO: MissionDTO) extends VBox:
    private def getCorrectColor(disabledColor: Color, supportColor: Color, defaultColor: Color): Color =
      if missionDTO.startingPurchaseCount != 0 && missionDTO.purchaseCount == 0
      then disabledColor
      else
        missionDTO.missionType match
          case Support => supportColor
          case _ => defaultColor

    import JfxTheme.*
    private val fillColor: Color = getCorrectColor(errorContainer, tertiaryContainer, primaryContainer)
    private val borderColor: Color = getCorrectColor(errorBorder, tertiaryBorder, primaryBorder)

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
    ).concat(Seq(missionDTO).flatMap(m =>
      if m.startingPurchaseCount > 0
      then Seq(new Text(s"${missionDTO.purchaseCount}/ ${missionDTO.startingPurchaseCount}"))
      else Seq.empty
    ))

    protected def button: Button =
      ButtonFactory.makeBoardButton(LanguageStrings.MissionPaneStrings.get, missionDTO.onClick, missionDTO.clickable)

    protected def rewards =
      new EffectWrapperPane(LanguageStrings.MissionPaneStrings.reward, missionDTO.rewards, borderColor)

    protected def cost =
      new EffectWrapperPane(LanguageStrings.MissionPaneStrings.cost, missionDTO.cost, borderColor)

    protected def name: Text = {
      val nameText = TextFactory.makeMissionName(missionDTO.id)
      Tooltip.install(nameText, new Tooltip(MissionDescriptions.getDescription(missionDTO)))
      nameText
    }

  class ObtainedMissionPane(missionDTO: MissionDTO) extends MissionPane(missionDTO):
    override protected def button: Button = ButtonFactory.makeBoardButton(
      LanguageStrings.MissionPaneStrings.supportGet,
      missionDTO.onClick,
      missionDTO.clickable
    )
  
  private class MissionCell(missions: Seq[MissionDTO], vertical: Boolean = false) extends HBox:
    if missions.nonEmpty
      then
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
    


