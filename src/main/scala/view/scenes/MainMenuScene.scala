package view.scenes

import controller.StandardViewState.{MainMenu, MatchInit}
import controller.{ControllerStage, Navigator, StandardViewState}
import javafx.event.ActionEvent
import scalafx.geometry.Pos.Center
import scalafx.scene.layout.*
import scalafx.scene.paint.Color
import scalafx.scene.{Node, Scene}
import scalafx.stage.{Popup, Stage}
import view.LanguageStrings.TitleScreenStrings as TSStrings
import view.ViewComponents.ViewScene
import view.buttons.FxButtonFactory
import view.text.FxTextFactory
import view.utils.FxPopup

class MainMenuScene(controllerStage: ControllerStage[StandardViewState]) extends ViewScene[Node] {
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
      FxTextFactory.makeMenuTitle,
      FxButtonFactory.makeMenuButton(TSStrings.startButtonText, () => controllerStage.changeScene(MatchInit)),
      FxButtonFactory.makeMenuButton(
        TSStrings.ruleButtonText,
        () => FxPopup.showPopUp()
      )
    )
  }
}
