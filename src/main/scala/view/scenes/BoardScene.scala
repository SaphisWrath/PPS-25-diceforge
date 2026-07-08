package view.scenes

import controller.GameController
import model.Players.Player
import scalafx.beans.property.ObjectProperty
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.Priority.Always
import scalafx.scene.layout.{BorderPane, HBox}
import scalafx.scene.{Node, Scene}
import view.builders.PlayerGUIComponentFactory
import view.buttons.ButtonFactory
import view.utils.ColorConversion.*

class BoardScene extends Scene:
  private val playerDirectors: Map[Player, PlayerGUIComponentFactory] =
    GameController.players.map(p => p -> PlayerGUIComponentFactory(p.getName, p.getColor.toScalaFX)).toMap

  private val activePlayer: ObjectProperty[Player] = new ObjectProperty(this, "activePlayer", GameController.activePlayer.get) {
    onChange((_, _, _) =>
      pane.top = nonActivePlayersPane()
      pane.bottom = activePlayerPane()
    )
  }

  private val pane = new BorderPane {
    top = nonActivePlayersPane()
    center = Label("center") //TODO
    bottom = activePlayerPane()
  }

  root = pane

  private def activePlayerPane(): Node =
    val playerBox = playerDirectors(activePlayer()).activePlayerBox
    val playerPane: HBox = new HBox {
      children = Seq(playerBox, nextTurnButton)
      spacing = 5
    }
    HBox.setHgrow(playerBox, Always)
    playerBox.maxWidth(Double.MaxValue)
    playerPane

  private def nonActivePlayersPane(): Node =
    val nonActivePlayerDirectors = GameController.nonActivePlayerList.map(playerDirectors(_))
    val playerBoxes: Seq[Node] = nonActivePlayerDirectors
        .map(_.nonActivePlayerBox)
    val pane: HBox = new HBox {
      children = playerBoxes
      spacing = 5
    }
    pane

  private val NEXT_TURN_BUTTON_TEXT = "Prossimo Turno"

  private def nextTurnButton: Button = ButtonFactory().makeBoardButton(
    NEXT_TURN_BUTTON_TEXT,
    event =>
      GameController.nextTurn()
      activePlayer() = GameController.activePlayer.get
  )