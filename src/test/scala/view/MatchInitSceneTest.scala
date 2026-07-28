package view

import controller.{ControllerMatchInit, ControllerStage, MatchBuilder}
import org.mockito.Mockito.mock
import _root_.mock.MockControllerStage
import controller.ViewState.MatchInit
import scalafx.application.JFXApp3
import view.TestStageSetup
import view.scenes.MatchInitScene

object MatchInitSceneTest extends JFXApp3:
  private val matchBuilder = mock[MatchBuilder]()
  override def start(): Unit = {
    stage = TestStageSetup(MatchInitScene(ControllerMatchInit(matchBuilder), MockControllerStage(MatchInit))).stage
  }