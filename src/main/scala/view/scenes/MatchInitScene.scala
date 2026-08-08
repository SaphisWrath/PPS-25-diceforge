package view.scenes

import controller.ViewState.Board
import controller.{ControllerMatchInit, ControllerStage}
import javafx.event.ActionEvent
import model.Players.Color
import model.Players.Color.*
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Pos.Center
import scalafx.scene.control.{ChoiceBox, Label, TextField}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.Node
import view.LanguageStrings.{separator, GameInitScreenStrings as GISStrings}
import view.ViewComponents.ViewScene
import view.buttons.ButtonFactory.makeMenuButton

class MatchInitScene(controller: ControllerMatchInit, controllerStage: ControllerStage) extends ViewScene[Node]:
  private val playerNameField = new TextField()
  private val playerColorChoice = new ChoiceBox[Color](ObservableBuffer[Color](Orange, Green, Blue, Black))
  private val feedbackLabel = new Label()
  private val addPlayerButton = makeMenuButton(GISStrings.addPlayerButtonText, addPlayerButtonAction())
  private val startMatchButton = makeMenuButton(GISStrings.startButtonText, _ => controllerStage.changeScene(Board))
  startMatchButton.disable = true

  private def makeRowWith(nodes: Iterable[Node]): HBox =
    new HBox {
      spacing = 20
      alignment = Center
      alignmentInParent = Center
      children = nodes
    }

  override def scene: Node = new VBox {
    fillWidth = true
    spacing = 20
    alignment = Center
    alignmentInParent = Center
    children = Seq(
      makeRowWith(Seq(new Label(GISStrings.playerNameLabelText + separator), playerNameField)),
      makeRowWith(Seq(new Label(GISStrings.playerColorLabelText + separator), playerColorChoice)),
      makeRowWith(Seq(addPlayerButton, startMatchButton)),
      makeRowWith(Seq(feedbackLabel))
    )
  }

  private def addPlayerButtonAction(): ActionEvent => Unit = _ =>
    controller.updateMatchInfo(playerNameField.getText, playerColorChoice.getValue)
    if controller.isLastPlayerValid
    then
      feedbackLabel.text = GISStrings.playerAddedConfirmationText
      playerNameField.text = ""
    else feedbackLabel.text = GISStrings.playerAddingErrorText

    if controller.enoughPlayers then startMatchButton.disable = false
    if controller.maxPlayers then addPlayerButton.disable = true
