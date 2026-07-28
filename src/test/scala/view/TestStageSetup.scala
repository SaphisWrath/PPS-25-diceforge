package view

import controller.ControllerMatchInit
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.layout.StackPane
import scalafx.scene.{Node, Scene}
import view.ViewComponents.ViewScene

class TestStageSetup(displayScene: ViewScene[Node]) {
  def stage: PrimaryStage = new PrimaryStage {
    title = "Test Setup"
    resizable = true
    minWidth = 800
    minHeight = 500
    width = minWidth
    height = minHeight
    scene = new Scene {
      root = new StackPane {
        children = displayScene.scene
      }
    }
  }
}
