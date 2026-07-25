import controller.{ControllerManager, ControllerStage, Navigator}
import scalafx.application.JFXApp3
import view.{FxMainStage, FxSceneFactory}

object ScalaFXHelloWorld extends JFXApp3 {
  override def start(): Unit = {
    val mainStage: FxMainStage = FxMainStage()
    val controllerManager: ControllerManager =
      ControllerManager(
        () => mainStage,
        controller => FxSceneFactory(controller)
      )
    ControllerStage.init(controllerManager.navigator)
    stage = mainStage.primaryStage
  }
}