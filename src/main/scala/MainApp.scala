import controller.{ControllerManager, ControllerStage, Navigator}
import scalafx.application.JFXApp3
import view.utils.FxPopup
import view.{FxMainStage, FxSceneFactory}

object MainApp extends JFXApp3 {
  override def start(): Unit = {
    val mainStage: FxMainStage = FxMainStage()
    val controllerManager: ControllerManager =
      ControllerManager(
        () => mainStage,
        controller => FxSceneFactory(controller)
      )
    controllerManager.stageController.init()
    FxPopup.setOwner(mainStage.primaryStage)
    stage = mainStage.primaryStage
  }
}