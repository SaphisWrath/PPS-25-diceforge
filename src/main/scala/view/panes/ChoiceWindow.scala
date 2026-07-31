package view.panes

import controller.PlayerChoice
import model.Players.Player
import scalafx.scene.control.{Button, Label}
import scalafx.geometry.Pos.Center
import scalafx.scene.layout.{BorderPane, HBox, Pane}
import view.buttons.ButtonFactory.makeChoiceButton

import java.util.concurrent.CountDownLatch

trait ChoiceWindow[A]:
  def pane: Pane
  def value: (Player, A)
  def stringSupplier_=(supplier: A => String): Unit

object ChoiceWindow:
  private class ChoiceWindowImpl[A](playerChoice: PlayerChoice[A], latch: CountDownLatch) extends ChoiceWindow[A]:
    var value: (Player, A) = (playerChoice._1, playerChoice._2.head)
    private var _stringSupplier: A => String = _ => "Somebody forgot to set the text thingy"

    override def pane: Pane =
      val buttons: Seq[Button] = playerChoice._2.map(option => makeChoiceButton(_stringSupplier(option), () => {
        this.value = (playerChoice._1, option)
        latch.countDown()
      }))

      new BorderPane {
        top = new Label(playerChoice._1.getName + ", scegli fra le seguenti opzioni")
        center = new HBox {
          alignment = Center
          children = buttons
        }
      }

    override def stringSupplier_=(supplier: A => String): Unit = _stringSupplier = supplier

  def apply[A](playerChoice: PlayerChoice[A], latch: CountDownLatch): ChoiceWindow[A] = ChoiceWindowImpl[A](playerChoice, latch)