package view.panes

import controller.dto.MissionDTO
import scalafx.geometry.{Insets, Pos}
import scalafx.geometry.Pos.Center
import scalafx.scene.control.{Button, Tooltip}
import scalafx.scene.layout.*
import scalafx.scene.paint.Color
import scalafx.scene.text.{Font, Text}
import view.{LanguageStrings, MissionDescriptions}
import view.buttons.FxButtonFactory
import view.panes.EffectPanes.EffectWrapperPane
import view.text.FxTextFactory
import view.theme.JfxTheme
import view.utils.ViewUtils
import view.utils.ViewUtils.{makeBackgroundFill, makeBorder}
import controller.dto.MissionType.*
import scalafx.scene.shape.Circle
import scalafx.scene.Node

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
    private val fillColor: Color = getCorrectColor(secondaryContainer, tertiaryContainer, primaryContainer)
    private val borderColor: Color = getCorrectColor(secondaryBorder, tertiaryBorder, primaryBorder)

    Tooltip.install(this, new Tooltip {
      text = MissionDescriptions.getDescription(missionDTO)
      font = new Font(14)
    })
    border = makeBorder(borderColor)
    background = makeBackgroundFill(fillColor)
    alignment = Center
    padding = Insets(2)
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
      FxButtonFactory.makeBoardButton(LanguageStrings.MissionPaneStrings.get, missionDTO.onClick, missionDTO.clickable)

    protected def rewards =
      EffectWrapperPane(LanguageStrings.MissionPaneStrings.reward, missionDTO.rewards, borderColor)

    protected def cost =
      EffectWrapperPane(LanguageStrings.MissionPaneStrings.cost, missionDTO.cost, borderColor)

    protected def name: Text = FxTextFactory.makeMissionName(MissionDescriptions.getTitle(missionDTO))

  class ObtainedMissionPane(missionDTO: MissionDTO) extends MissionPane(missionDTO):
    override protected def button: Button = FxButtonFactory.makeBoardButton(
      LanguageStrings.MissionPaneStrings.supportGet,
      missionDTO.onClick,
      missionDTO.clickable
    )
  
  private class MissionCell(missions: Seq[MissionDTO], playerToken: Option[Node] = Option.empty) extends VBox:
    border = makeBorder(JfxTheme.primaryBorder)
    padding = Insets(5)
    alignment = Pos.Center
    children = Seq(
      new HBox {
        spacing = 10
        children = missions.map(MissionPane(_))
      },
      playerToken.getOrElse(Circle(10, Color.Transparent))
    )

  class MissionBoardPane(missions: Map[Int, Seq[MissionDTO]], playerTokens: Map[Int, Node]) extends BorderPane:
    private val contentSpacing: Double = 20
    padding = Insets(10)
    top = new HBox {
      alignment = Center
      spacing = contentSpacing
      children = Seq(
        MissionCell(missions(0), playerTokens.get(0)),
        MissionCell(missions(1), playerTokens.get(1)),
        MissionCell(missions(2), playerTokens.get(2)),
      )
    }
    bottom = new HBox {
      spacing = contentSpacing
      alignment = Center
      children = Seq(
        MissionCell(missions(3), playerTokens.get(3)),
        MissionCell(missions(4), playerTokens.get(4)),
        MissionCell(missions(5), playerTokens.get(5)),
        MissionCell(missions(6), playerTokens.get(6))
      )
    }
    


