package view.scenes

import controller.ControllerMatchInit
import model.Players.Color
import model.Players.Color.*
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Pos.Center
import scalafx.scene.{Node, Scene}
import scalafx.scene.control.{Button, ChoiceBox, Label, TextField}
import scalafx.scene.layout.{HBox, VBox}
import view.LanguageStrings.{separator, GameInitScreenStrings as GISStrings}
import view.ViewComponents.ViewScene

class MatchInitScene(controller: ControllerMatchInit) extends ViewScene[Node]:
  private val playerCountChoice = new ChoiceBox[Int](ObservableBuffer[Int](2, 3, 4))
  private val playerNameField = new TextField()
  private val playerColorChoice = new ChoiceBox[Color](ObservableBuffer[Color](Orange, Green, Blue, Black))
  private val feedbackLabel = new Label()
  private val addPlayerButton = new Button(GISStrings.addPlayerButtonText)
  
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
      makeRowWith(Seq(new Label(GISStrings.howManyPlayersText), playerCountChoice)),
      makeRowWith(Seq(new Label(GISStrings.playerNameLabelText + separator), playerNameField)),
      makeRowWith(Seq(new Label(GISStrings.playerColorLabelText + separator), playerColorChoice)),
      makeRowWith(Seq(addPlayerButton)),
      makeRowWith(Seq(feedbackLabel))
    )
  }

  addPlayerButton.onAction = _ => {
    if !controller.isPlayerAmountSet
    then
      controller.setPlayerAmount(playerCountChoice.getValue)
      playerCountChoice.disable = true

    controller.updateMatchInfo(playerNameField.getText, playerColorChoice.getValue)
    if controller.isLastPlayerValid
    then feedbackLabel.text = GISStrings.playerAddedConfirmationText
    else feedbackLabel.text = GISStrings.playerAddingErrorText

    if controller.allPlayersSet
    then feedbackLabel.text = GISStrings.gameReadyConfirmationText
  }
