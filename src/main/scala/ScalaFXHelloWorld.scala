import controller.{ControllerStage, Navigator}
import scalafx.application.JFXApp3
import view.{FxMainStage, FxSceneFactory}

object ScalaFXHelloWorld extends JFXApp3 {
  override def start(): Unit = {
    val mainStage = FxMainStage()
    val viewFactory = FxSceneFactory()
    val navigator = Navigator(mainStage, viewFactory)
    ControllerStage.init(navigator)
    stage = mainStage.primaryStage
  }
}