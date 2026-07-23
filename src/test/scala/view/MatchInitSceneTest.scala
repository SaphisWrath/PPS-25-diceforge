package view

import controller.ControllerMatchInit
import scalafx.application.JFXApp3
import view.TestStageSetup
import view.scenes.MatchInitScene

object MatchInitSceneTest extends JFXApp3:
  override def start(): Unit = {
    stage = TestStageSetup(MatchInitScene(ControllerMatchInit)).stage
  }