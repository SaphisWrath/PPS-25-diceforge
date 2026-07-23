package view

object ViewComponents:
  trait MainStage[T]:
    def setContent(scene: ViewScene[T]): Unit

  trait ViewScene[T]:
    def scene: T

  trait ViewSceneFactory[T]:
    def createMainMenuScene(): ViewScene[T]
    def createMatchInitScene(): ViewScene[T]
    def createBoardScene(): ViewScene[T]
    def createMatchEndScene(): ViewScene[T]
