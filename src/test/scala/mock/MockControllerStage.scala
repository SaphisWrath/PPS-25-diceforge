package mock

import controller.{ControllerStage, ViewState}

class MockControllerStage(private var view: ViewState) extends ControllerStage:
  override def init(): Unit = print("initialized")

  override def changeScene(newState: ViewState): Unit =
    view = newState
    println(s"Changed Scene to: $view")

  override def currentViewState: ViewState = view
