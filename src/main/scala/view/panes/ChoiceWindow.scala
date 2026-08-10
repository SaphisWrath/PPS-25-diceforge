package view.panes

import controller.PlayerChoice
import controller.dto.PlayerDTO
import scalafx.scene.control.Label
import scalafx.geometry.Pos.Center
import scalafx.scene.{Node, Scene}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.stage.{Modality, Stage, StageStyle}
import view.LanguageStrings
import view.buttons.ButtonFactory.makeChoiceButton

trait ChoiceWindow[A]:
  def show(mapper: A => Node): Unit
  def buttonsAvailable: Boolean
  def forceNext(): Unit

object ChoiceWindowChain:
  private class ChoiceWindowChainImpl[A](playerChoices: Seq[PlayerChoice[A]],
                                         results: Seq[Int],
                                         next: (Seq[Int], Seq[PlayerChoice[A]]) => Unit,
                                         orElse: Seq[Int] => Unit) extends ChoiceWindow[A]:

    private val playerChoice = playerChoices.head

    private def buttonCallback(currentResults: Seq[Int], closeCall: () => Unit): () => Unit =
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
              Label(s"${playerChoice._1.name}, ${LanguageStrings.Miscellaneous.choiceDialog}"),
              new HBox {
                alignment = Center
                children = playerChoice._2.map(option => makeChoiceButton(
                  mapper(option),
                  buttonCallback(results.concat(Seq(playerChoice._2.indexOf(option))), close))
                )
              }
            )
          }
        }
      }

      popupStage.showAndWait()

    override def buttonsAvailable: Boolean = playerChoice._2.nonEmpty
    override def forceNext(): Unit = buttonCallback(results, println)()

  def manageChoices[A](choices: Seq[PlayerChoice[A]], orElse: Seq[Int] => Unit, mapper: A => Node): Unit =
    def nextChoiceWindow(results: Seq[Int], playerChoices: Seq[PlayerChoice[A]]): Unit =
      val popup = ChoiceWindowChain(playerChoices, results, nextChoiceWindow, orElse)
      popup.show(mapper)
      if !popup.buttonsAvailable then popup.forceNext()

    if choices.isEmpty
    then orElse(Seq.empty)
    else nextChoiceWindow(Seq.empty, choices)

  def apply[A](playerChoices: Seq[PlayerChoice[A]],
               results: Seq[Int],
               next: (Seq[Int], Seq[PlayerChoice[A]]) => Unit,
               orElse: Seq[Int] => Unit): ChoiceWindow[A] = ChoiceWindowChainImpl[A](playerChoices, results, next, orElse)