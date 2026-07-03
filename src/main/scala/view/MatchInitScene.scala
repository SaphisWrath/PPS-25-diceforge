package view

import controller.ControllerMatchInit
import model.Players.Color
import model.Players.Color.*
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Pos.Center
import scalafx.scene.Node
import scalafx.scene.Scene
import scalafx.scene.control.{Button, ChoiceBox, Label, TextField}
import scalafx.scene.layout.{HBox, VBox}

class MatchInitScene(controller: ControllerMatchInit) extends Scene:
  private val playerCountChoice = new ChoiceBox[Int](ObservableBuffer[Int](2, 3, 4))
  private val playerNameField = new TextField()
  private val playerColorChoice = new ChoiceBox[Color](ObservableBuffer[Color](Orange, Green, Blue, Black))
  private val feedbackLabel = new Label()
  private val addPlayerButton = new Button("Add player")
  
  private def makeRowWith(nodes: Iterable[Node]): HBox =
    new HBox {
      spacing = 20
      alignment = Center
      alignmentInParent = Center
      children = nodes
    }
    
  root = new VBox {
    fillWidth = true
    spacing = 20
    alignment = Center
    alignmentInParent = Center
    children = Seq(
      makeRowWith(Seq(new Label("How many players?"), playerCountChoice)),
      makeRowWith(Seq(new Label("Player name:"), playerNameField)),
      makeRowWith(Seq(new Label("Player color:"), playerColorChoice)),
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
    then feedbackLabel.text = "Player added!"
    else feedbackLabel.text = "Name or color already picked"

    if controller.allPlayersSet
    then feedbackLabel.text = "Ready to start the game!"
  }
