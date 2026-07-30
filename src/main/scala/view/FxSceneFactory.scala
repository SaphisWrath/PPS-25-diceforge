package view

import controller.ControllerManager
import scalafx.scene.Node
import view.ViewComponents.{ViewScene, ViewSceneFactory}
import view.scenes.{BoardScene, MainMenuScene, MatchEndScene, MatchInitScene}

class FxSceneFactory(controllerManager: ControllerManager) extends ViewSceneFactory[Node](controllerManager):
  override def createMainMenuScene(): ViewScene[Node] = MainMenuScene(controllerManager.stageController)
  override def createMatchInitScene(): ViewScene[Node] = MatchInitScene(controllerManager.matchInitController, controllerManager.stageController)
  override def createBoardScene(): ViewScene[Node] = 
    BoardScene(controllerManager.gameController, controllerManager.stageController)
  override def createMatchEndScene(): ViewScene[Node] = MatchEndScene(controllerManager.matchEndController, controllerManager.stageController)
