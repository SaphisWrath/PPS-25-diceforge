package view

import scalafx.application.JFXApp3.PrimaryStage

class MainStage(/* TODO controller */) {
  def stage: PrimaryStage = new PrimaryStage {
    title = "Dice Forge"
    resizable = true
    minWidth = 800
    minHeight = 500
    width = 800
    height = 500
    scene = new MainMenuScene()
  }
}
