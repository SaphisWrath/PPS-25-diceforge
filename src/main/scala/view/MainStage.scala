package view

import controller.ControllerStage
import scalafx.application.JFXApp3.PrimaryStage
import view.scenes.MainMenuScene

class MainStage(controller: ControllerStage) {
  def stage: PrimaryStage = new PrimaryStage {
    title = "Dice Forge"
    resizable = true
    minWidth = 800
    minHeight = 500
    width = 800
    height = 500
  }
}
