import controller.ControllerStage
import scalafx.application.JFXApp3
import view.FxMainStage

object ScalaFXHelloWorld extends JFXApp3 {
  override def start(): Unit = {
    val controller = ControllerStage()
    stage = controller.init()
  }
}