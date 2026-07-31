package view.panes

import controller.PlayerChoice
import model.Players.Player
import scalafx.scene.control.{Button, Label}
import scalafx.geometry.Pos.Center
import scalafx.scene.layout.{BorderPane, HBox, Pane}
import view.buttons.ButtonFactory.makeChoiceButton

trait ChoiceWindow[A]:
  def pane: Pane
  def stringSupplier_=(supplier: A => String): Unit

object ChoiceWindow:
  private class ChoiceWindowImpl[A](playerChoices: Seq[PlayerChoice[A]],
                                    results: Seq[(Player, A)],
                                    next: (Seq[(Player, A)], Seq[PlayerChoice[A]]) => Unit,
                                    orElse: Seq[(Player, A)] => Unit) extends ChoiceWindow[A]:

    private val playerChoice = playerChoices.head
    private var _stringSupplier: A => String = _ => "Somebody forgot to set the text thingy"

    override def pane: Pane =
      val buttons: Seq[Button] = playerChoice._2.map(option => makeChoiceButton(_stringSupplier(option), () => {
        val newResults = results.concat(Seq((playerChoice._1, option)))
        if playerChoices.tail.isEmpty
          then orElse(newResults)
        else next(newResults, playerChoices.tail)
      }))

      new BorderPane {
        top = new Label(playerChoice._1.getName + ", scegli fra le seguenti opzioni")
        center = new HBox {
          alignment = Center
          children = buttons
        }
      }

    override def stringSupplier_=(supplier: A => String): Unit = _stringSupplier = supplier

  def apply[A](playerChoices: Seq[PlayerChoice[A]],
               results: Seq[(Player, A)],
               next: (Seq[(Player, A)], Seq[PlayerChoice[A]]) => Unit,
               orElse: Seq[(Player, A)] => Unit): ChoiceWindow[A] = ChoiceWindowImpl[A](playerChoices, results, next, orElse)