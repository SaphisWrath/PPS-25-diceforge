package view.scenes

import controller.ControllerMainMenu
import javafx.event.ActionEvent
import scalafx.geometry.Pos.Center
import scalafx.scene.{Node, Scene}
import scalafx.scene.layout.*
import scalafx.scene.paint.Color
import view.buttons.ButtonFactory
import view.text.TextFactory
import view.LanguageStrings.TitleScreenStrings as TSStrings
import view.ViewComponents.ViewScene

class MainMenuScene(onStart: ActionEvent => Unit, onRules: ActionEvent => Unit) extends ViewScene[Node] {
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
      ButtonFactory.makeMenuButton(TSStrings.startButtonText, onStart),
      ButtonFactory.makeMenuButton(TSStrings.ruleButtonText, onRules)
    )
  }
}
