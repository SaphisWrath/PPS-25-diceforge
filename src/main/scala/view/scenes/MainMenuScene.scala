package view.scenes

import controller.ViewState.{MainMenu, MatchInit}
import controller.{ControllerStage, Navigator}
import javafx.event.ActionEvent
import scalafx.geometry.Pos.Center
import scalafx.scene.layout.*
import scalafx.scene.paint.Color
import scalafx.scene.{Node, Scene}
import view.LanguageStrings.TitleScreenStrings as TSStrings
import view.ViewComponents.ViewScene
import view.buttons.ButtonFactory
import view.text.TextFactory

class MainMenuScene(controllerStage: ControllerStage) extends ViewScene[Node] {
  override def scene: Node = new VBox {
    fillWidth = true
    spacing = 20
    border = new Border(new BorderStroke(
      Color.Black,
      BorderStrokeStyle.Solid,
      CornerRadii.Empty,
      BorderWidths.Default)
    )
    alignment = Center
    alignmentInParent = Center
    children = Seq(
      TextFactory.makeMenuTitle,
      ButtonFactory.makeMenuButton(TSStrings.startButtonText, _ => controllerStage.changeScene(MatchInit)),
      ButtonFactory.makeMenuButton(TSStrings.ruleButtonText, _ => controllerStage.changeScene(MainMenu))
    )
  }
}
