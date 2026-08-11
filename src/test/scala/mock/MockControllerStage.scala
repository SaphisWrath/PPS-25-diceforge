package mock

import controller.{ControllerStage, StandardViewState}

class MockControllerStage(private var view: StandardViewState) extends ControllerStage[StandardViewState]:
  override def init(): Unit = print("initialized")

  override def changeScene(newState: StandardViewState): Unit =
    view = newState
    println(s"Changed Scene to: $view")

  override def currentViewState: StandardViewState = view
