package view

import controller.ControllerMatchEnd
import controller.MockGameMatches.mockGameMatch
import scalafx.application.JFXApp3
import view.TestStageSetup
import view.scenes.MatchEndScene

object MatchEndSceneTest extends JFXApp3:
  override def start(): Unit =
    val controllerEnd = ControllerMatchEnd(mockGameMatch)
    stage = TestStageSetup(MatchEndScene(controllerEnd)).stage