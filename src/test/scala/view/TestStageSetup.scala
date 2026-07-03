package view

import controller.ControllerMatchInit
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene

class TestStageSetup(displayScene: Scene) {
  def stage: PrimaryStage = new PrimaryStage {
    title = "Test Setup"
    resizable = true
    minWidth = 800
    minHeight = 500
    width = minWidth
    height = minHeight
    scene = displayScene
  }
}
