package view

import controller.ControllerMatchInitImpl
import scalafx.application.JFXApp3
import view.{MainStage, MatchInitStage}

object MatchInitSceneTest extends JFXApp3:
  override def start(): Unit = {
    stage = MatchInitStage(ControllerMatchInitImpl()).stage
  }