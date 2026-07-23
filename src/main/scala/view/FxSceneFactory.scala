package view

import controller.{ControllerMatchEnd, ControllerMatchEndImpl, ControllerMatchInit, ControllerMatchInitImpl, ControllerProvider}
import scalafx.scene.Node
import view.ViewComponents.{ViewScene, ViewSceneFactory}
import view.scenes.{BoardScene, MainMenuScene, MatchEndScene, MatchInitScene}

class FxSceneFactory extends ViewSceneFactory[Node]:
  override def createMainMenuScene(): ViewScene[Node] = MainMenuScene()
  override def createMatchInitScene(): ViewScene[Node] = MatchInitScene(ControllerProvider.controllerMatchInit)
  override def createBoardScene(): ViewScene[Node] = BoardScene()
  override def createMatchEndScene(): ViewScene[Node] = MatchEndScene(ControllerProvider.controllerMatchEnd)
