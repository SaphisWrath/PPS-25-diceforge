package view

import controller.StandardViewState.*
import controller.{ControllerManager, StandardViewState}
import scalafx.scene.Node
import view.ViewComponents.{ViewScene, ViewSceneFactory}
import view.scenes.{BoardScene, MainMenuScene, MatchEndScene, MatchInitScene}

class FxSceneFactory(controllerManager: ControllerManager) extends ViewSceneFactory[Node, StandardViewState](controllerManager):
  override def createScene(viewState: StandardViewState): ViewScene[Node] = viewState match
    case MainMenu => MainMenuScene(controllerManager.stageController)
    case MatchInit => MatchInitScene(controllerManager.matchInitController, controllerManager.stageController)
    case Board => BoardScene(controllerManager.gameController, controllerManager.stageController)
    case MatchEnd => MatchEndScene(controllerManager.matchEndController, controllerManager.stageController)
