package view.scenes

import controller.ViewState.Board
import controller.dto.PlayerDTO
import controller.{ControllerMatchInit, ControllerStage}
import javafx.event.ActionEvent
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Pos.Center
import scalafx.scene.control.{ChoiceBox, Label, TextField}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.Node
import scalafx.scene.paint.Color
import view.LanguageStrings.{separator, GameInitScreenStrings as GISStrings}
import view.Redrawable
import view.ViewComponents.ViewScene
import view.builders.PlayerBoxes.{PlayerBoxBuilder, PlayerBoxStyle}
import view.buttons.ButtonFactory.makeMenuButton

class MatchInitScene(controller: ControllerMatchInit, controllerStage: ControllerStage) extends ViewScene[Node]:
  private val playerNameField = new TextField()
  private val playerColorChoice = new ChoiceBox[String](ObservableBuffer[String]("Orange", "Green", "Blue", "Black"))
  playerColorChoice.value = "Orange"
  private val feedbackLabel = new Label()
  private var playerList: Seq[Node] = Seq.empty
  private val playerQueue = Redrawable { () => makeRowWith(playerList) }
  private val addPlayerButton = makeMenuButton(GISStrings.addPlayerButtonText, addPlayerButtonAction())
  private val startMatchButton = makeMenuButton(GISStrings.startButtonText, _ => controllerStage.changeScene(Board))
  startMatchButton.disable = true

  private def addPlayerBox(playerDTO: PlayerDTO): Unit =
    playerList = playerList.concat(Seq(PlayerBoxBuilder(PlayerBoxStyle.Small)
      .withNameSection(playerDTO.name)
      .withCircleTokenSection(Color.valueOf(playerDTO.colorHex), 10)
      .build
    ))

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
      feedbackLabel,
      playerQueue.component
    )
  }

  private def addPlayerButtonAction(): ActionEvent => Unit = _ =>
    controller.updateMatchInfo(playerNameField.getText, playerColorChoice.getValue)
    if controller.isLastPlayerValid
    then
      addPlayerBox(controller.currentPlayers.takeRight(1).head)
      playerQueue.redraw()
      feedbackLabel.text = GISStrings.playerAddedConfirmationText
      playerNameField.text = ""
    else
      if playerNameField.getText == ""
      then feedbackLabel.text = GISStrings.absentNameErrorText
      else feedbackLabel.text = GISStrings.playerAddingErrorText

    if controller.enoughPlayers then startMatchButton.disable = false
    if controller.maxPlayers then addPlayerButton.disable = true
