package view

import controller.{ControllerMatchEnd, ControllerStage}
import controller.MockGameMatches.mockGameMatch
import org.mockito.Mockito.mock
import _root_.mock.MockControllerStage
import controller.ViewState.MatchEnd
import scalafx.application.JFXApp3
import view.TestStageSetup
import view.scenes.MatchEndScene

object MatchEndSceneTest extends JFXApp3:

  override def start(): Unit =
    val controllerEnd = ControllerMatchEnd(mockGameMatch)
    stage = TestStageSetup(MatchEndScene(controllerEnd, MockControllerStage(MatchEnd))).stage