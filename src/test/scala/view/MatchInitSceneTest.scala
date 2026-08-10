package view

import _root_.mock.MockControllerStage
import controller.ViewState.MatchInit
import controller.ControllerMatchInit
import scalafx.application.JFXApp3
import view.TestStageSetup
import view.scenes.MatchInitScene

object MatchInitSceneTest extends JFXApp3:

  override def start(): Unit = {
    stage = TestStageSetup(MatchInitScene(ControllerMatchInit(), MockControllerStage(MatchInit))).stage
  }