package view.panes

import controller.converters.ResourceConverters
import controller.dto.{EffectDTO, MissionDTO}
import scalafx.event.ActionEvent
import scalafx.geometry.Insets
import scalafx.geometry.Pos.{Center, CenterLeft}
import scalafx.scene.{Node, Parent}
import scalafx.scene.layout.{Border, BorderStroke, BorderStrokeStyle, BorderWidths, CornerRadii, GridPane, HBox, Pane, VBox}
import scalafx.scene.paint.Color
import view.buttons.ButtonFactory
import view.scenes.ViewComponent
import view.text.TextFactory

class MissionPane(missionDTO: MissionDTO) extends ViewComponent:
  override def component: Node =
    new VBox {
      alignment = Center
      padding = Insets(10)
      children = Seq(
        TextFactory.makeMissionName(missionDTO.id),
        new EffectWrapperPane("Costo", missionDTO.cost).component,
        new EffectWrapperPane("Ricompensa", missionDTO.rewards).component
      )
    }

class MissionCell extends ViewComponent:
  val gridPane = new GridPane()
  override def component: Node = gridPane
    
    


