package view.scenes

import controller.ControllerMainMenu
import javafx.event.ActionEvent
import scalafx.geometry.Pos.Center
import scalafx.scene.Scene
import scalafx.scene.layout.*
import scalafx.scene.paint.Color
import view.buttons.ButtonFactory
import view.text.TextFactory

class MainMenuScene(onStart: ActionEvent => Unit, onRules: ActionEvent => Unit) extends Scene {
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
      TextFactory.makeMenuTitle,
      ButtonFactory.makeMenuButton("INIZIA", onStart),
      ButtonFactory.makeMenuButton("REGOLE", onRules)
    )
  }
}
