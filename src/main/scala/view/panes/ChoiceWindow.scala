package view.panes

import controller.PlayerChoice
import model.Players.Player
import scalafx.scene.control.{Button, Label}
import scalafx.geometry.Pos.Center
import scalafx.scene.Node
import scalafx.scene.layout.{BorderPane, HBox, Pane, VBox}
import view.buttons.ButtonFactory.makeChoiceButton
import view.scenes.ViewComponent

trait ChoiceWindow[A]:
  def pane: Pane
  def setMapper(map: A => ViewComponent): Unit

object ChoiceWindowChain:
  private class ChoiceWindowChainImpl[A](playerChoices: Seq[PlayerChoice[A]],
                                    results: Seq[(Player, A)],
                                    next: (Seq[(Player, A)], Seq[PlayerChoice[A]]) => Unit,
                                    orElse: Seq[(Player, A)] => Unit) extends ChoiceWindow[A]:

    private val playerChoice = playerChoices.head
    private var mapper: A => ViewComponent = _ => ???

    override def pane: Pane =
      val buttons: Seq[Button] = playerChoice._2.map(option => makeChoiceButton(mapper(option), () => {
        val newResults = results.concat(Seq((playerChoice._1, option)))
        if playerChoices.tail.isEmpty
          then orElse(newResults)
        else next(newResults, playerChoices.tail)
      }))

      new BorderPane {
        center = new VBox {
          alignment = Center
          children = Seq(
            Label(playerChoice._1.name + ", scegli fra le seguenti opzioni"),
            new HBox {
              alignment = Center
              children = buttons
            }
          )
        }
      }

    override def setMapper(map: A => ViewComponent): Unit = mapper = map
  
  def apply[A](playerChoices: Seq[PlayerChoice[A]],
               results: Seq[(Player, A)],
               next: (Seq[(Player, A)], Seq[PlayerChoice[A]]) => Unit,
               orElse: Seq[(Player, A)] => Unit): ChoiceWindow[A] = ChoiceWindowChainImpl[A](playerChoices, results, next, orElse)