package view

import _root_.mock.MockControllerStage
import controller.MockGameMatches.mockGameMatch
import controller.ViewState.MatchEnd
import controller.{ControllerMatchEnd, ControllerStage}
import org.mockito.Mockito.mock
import scalafx.application.JFXApp3
import view.TestStageSetup
import view.scenes.MatchEndScene

object MatchEndSceneTest extends JFXApp3:

  override def start(): Unit =
    val controllerEnd = ControllerMatchEnd(mockGameMatch)
    stage = TestStageSetup(MatchEndScene(controllerEnd, MockControllerStage(MatchEnd))).stage