package view

import controller.ControllerMatchInit
import scalafx.application.JFXApp3.PrimaryStage

class MatchInitStage(controller: ControllerMatchInit) {
  def stage: PrimaryStage = new PrimaryStage {
    title = "Dice Forge - Match Setup"
    resizable = true
    minWidth = 800
    minHeight = 500
    width = minWidth
    height = minHeight
    scene = new MatchInitScene(controller)
  }
}
