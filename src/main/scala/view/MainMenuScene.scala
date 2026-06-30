package view

import javafx.event.EventHandler
import scalafx.beans.property.IntegerProperty
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.VBox

class MainMenuScene extends Scene{
  content = new VBox {
    val counter = IntegerProperty(0)
    val label: Label = new Label {
      text = counter().toString
    }
    counter.onChange((_, _, newValue) => {
      label.text = newValue.toString
    })
    children = Seq(
      label,
      new Button ("COUNT"){
        onAction = { _ => counter() = counter() + 1}
        minWidth = 100
        minHeight = 50
      },
      new Button ("INIZIA") {
        minWidth = 100
        minHeight = 50
      },
      new Button ("REGOLE") {
        minWidth = 100
        minHeight = 50
      }
    )
  }
}
