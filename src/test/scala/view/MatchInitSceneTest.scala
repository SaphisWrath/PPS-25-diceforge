package view

import controller.ControllerMatchInitImpl
import scalafx.application.JFXApp3
import view.TestStageSetup

object MatchInitSceneTest extends JFXApp3:
  override def start(): Unit = {
    stage = TestStageSetup(MatchInitScene(ControllerMatchInitImpl())).stage
  }