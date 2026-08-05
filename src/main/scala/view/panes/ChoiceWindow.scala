package view.panes

import controller.PlayerChoice
import model.Players.Player
import scalafx.scene.control.{Button, Label}
import scalafx.geometry.Pos.Center
import scalafx.scene.{Node, Scene}
import scalafx.scene.layout.{BorderPane, HBox, Pane, VBox}
import scalafx.stage.{Modality, Stage, StageStyle}
import view.buttons.ButtonFactory.makeChoiceButton

trait ChoiceWindow[A]:
  def show(mapper: A => Node): Unit
  def buttonsAvailable: Boolean
  def forceNext(): Unit

object ChoiceWindowChain:
  private class ChoiceWindowChainImpl[A](playerChoices: Seq[PlayerChoice[A]],
                                    results: Seq[(Player, A)],
                                    next: (Seq[(Player, A)], Seq[PlayerChoice[A]]) => Unit,
                                    orElse: Seq[(Player, A)] => Unit) extends ChoiceWindow[A]:

    private val playerChoice = playerChoices.head

    private def buttonCallback(currentResults: Seq[(Player, A)], closeCall: () => Unit): () => Unit =
      () => {
        if playerChoices.tail.isEmpty
        then orElse(currentResults)
        else next(currentResults, playerChoices.tail)
        closeCall()
      }

    override def show(mapper: A => Node): Unit =
      val popupStage = new Stage {
        initStyle(StageStyle.Undecorated)
        initModality(Modality.ApplicationModal)

        scene = new Scene(500, 300) {
          root = new VBox {
            alignment = Center
            children = Seq(
              Label(playerChoice._1.name + ", scegli fra le seguenti opzioni"),
              new HBox {
                alignment = Center
                children = playerChoice._2.map(option => makeChoiceButton(
                  mapper(option),
                  buttonCallback(results.concat(Seq((playerChoice._1, option))), close))
                )
              }
            )
          }
        }
      }

      popupStage.showAndWait()

    override def buttonsAvailable: Boolean = playerChoice._2.nonEmpty
    override def forceNext(): Unit = buttonCallback(results, println)()

  def apply[A](playerChoices: Seq[PlayerChoice[A]],
               results: Seq[(Player, A)],
               next: (Seq[(Player, A)], Seq[PlayerChoice[A]]) => Unit,
               orElse: Seq[(Player, A)] => Unit): ChoiceWindow[A] = ChoiceWindowChainImpl[A](playerChoices, results, next, orElse)