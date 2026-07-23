package view

import controller.ControllerStage
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.beans.property.ObjectProperty
import scalafx.scene.{Node, Scene}
import scalafx.scene.layout.StackPane
import view.scenes.MainMenuScene
import view.ViewComponents.{MainStage, ViewScene}

class FxMainStage extends MainStage[Node]:
  private val paneContent: ObjectProperty[Node] = ObjectProperty[Node](new StackPane())
  private val mainPane = new StackPane {
    children = paneContent()
  }
  
  paneContent.onChange((_,_,newVal) => mainPane.children = newVal)
  
  val primaryStage: PrimaryStage = new PrimaryStage {
    title = "Dice Forge"
    scene = new Scene {
      root = mainPane
    }
    resizable = true
    minWidth = 800
    minHeight = 500
    width = 800
    height = 500
  }
  
  override def setContent(scene: ViewScene[Node]): Unit = paneContent() = scene()
