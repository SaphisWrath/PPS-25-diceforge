package view

import controller.{ControllerManager, ViewState}

object ViewComponents:
  trait MainStage[T]:
    def setContent(scene: ViewScene[T]): Unit

  trait ViewScene[T]:
    def scene: T

    def apply(): T = this.scene

  trait ViewSceneFactory[T, VS <: ViewState](controllerManager: ControllerManager):
    def createScene(viewState: VS): ViewScene[T]