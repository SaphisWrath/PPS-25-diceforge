package view

import controller.{ControllerMatchEnd, ControllerMatchEndImpl, ControllerMatchInit, ControllerMatchInitImpl, ControllerManager}
import scalafx.scene.Node
import view.ViewComponents.{ViewScene, ViewSceneFactory}
import view.scenes.{BoardScene, MainMenuScene, MatchEndScene, MatchInitScene}

class FxSceneFactory(controllerManager: ControllerManager) extends ViewSceneFactory[Node](controllerManager):
  override def createMainMenuScene(): ViewScene[Node] = MainMenuScene(controllerManager.navigator)
  override def createMatchInitScene(): ViewScene[Node] = MatchInitScene(controllerManager.controllerMatchInit)
  override def createBoardScene(): ViewScene[Node] = 
    BoardScene(controllerManager.gameController, controllerManager.navigator)
  override def createMatchEndScene(): ViewScene[Node] = MatchEndScene(controllerManager.controllerMatchEnd)
