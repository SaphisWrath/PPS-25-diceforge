import scalafx.application.JFXApp3
import view.MainStage

object ScalaFXHelloWorld extends JFXApp3 {
  override def start(): Unit = {
    stage = MainStage().stage
  }
}