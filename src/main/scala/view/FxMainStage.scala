package view

import controller.ControllerStage
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.beans.property.ObjectProperty
import scalafx.scene.layout.StackPane
import scalafx.scene.{Node, Scene}
import view.ViewComponents.{MainStage, ViewScene}
import view.scenes.MainMenuScene

class FxMainStage extends MainStage[Node]:
  private val paneContent: ObjectProperty[Node] = ObjectProperty[Node](new StackPane())
  private val mainPane = new StackPane {
    children = paneContent()
  }

  paneContent.onChange((_, _, newVal) => mainPane.children = newVal)

  val primaryStage: PrimaryStage = new PrimaryStage {
    title = "Dice Forge"
    scene = new Scene {
      root = mainPane
    }
    maximized = true
    resizable = false
  }

  override def setContent(scene: ViewScene[Node]): Unit = paneContent() = scene()
