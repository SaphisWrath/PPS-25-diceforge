package view

import _root_.mock.MockControllerStage
import controller.MockGameMatches.*
import controller.ViewState.MatchEnd
import controller.ControllerMatchEnd
import scalafx.application.JFXApp3
import view.TestStageSetup
import view.scenes.MatchEndScene

object MatchEndSceneTest extends JFXApp3:

  override def start(): Unit =
    val controllerEnd = ControllerMatchEnd(mockGameMatchDraw)
    stage = TestStageSetup(MatchEndScene(controllerEnd, MockControllerStage(MatchEnd))).stage