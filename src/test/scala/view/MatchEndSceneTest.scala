package view

import controller.ControllerMatchEnd
import scalafx.application.JFXApp3
import view.TestStageSetup
import view.scenes.MatchEndScene
import controller.MockControllers.mockGameController

object MatchEndSceneTest extends JFXApp3:
  override def start(): Unit =
    val controllerEnd = ControllerMatchEnd
    controllerEnd.gameController = mockGameController
    stage = TestStageSetup(MatchEndScene()).stage