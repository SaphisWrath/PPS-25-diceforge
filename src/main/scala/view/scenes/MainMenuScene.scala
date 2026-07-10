package view.scenes

import controller.ControllerMainMenu
import scalafx.geometry.Pos.Center
import scalafx.scene.Scene
import scalafx.scene.layout.*
import scalafx.scene.paint.Color
import view.buttons.ButtonFactory
import view.text.TextFactory

class MainMenuScene(controller: ControllerMainMenu) extends Scene {
  private val buttonFactory: ButtonFactory = ButtonFactory()
  private val textFactory: TextFactory = TextFactory()
  root = new VBox {
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
      textFactory.makeMenuTitle,
      buttonFactory.makeMenuButton("INIZIA", controller.onStart),
      buttonFactory.makeMenuButton("REGOLE", controller.onRules)
    )
  }
}
