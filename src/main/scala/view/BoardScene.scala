package view

import controller.GameController
import model.Players.Player
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.Priority.Always
import scalafx.scene.layout.{BorderPane, HBox}
import scalafx.scene.{Node, Scene}
import view.builders.PlayerGUIComponentFactory
import view.utils.ColorConversion.*

class BoardScene extends Scene:
  private val playerDirectors: Map[Player, PlayerGUIComponentFactory] =
    GameController.players.map(p => p -> PlayerGUIComponentFactory(p.getName, p.getColor.toScalaFX)).toMap

  root = new BorderPane {
    top = nonActivePlayersPane()
    center = Label("center")
    bottom = activePlayerPane()
  }

  private def activePlayerPane(): Node =
    val activePlayer = GameController.activePlayer.get
    val playerBox = playerDirectors(activePlayer).activePlayerBox.create
    val nextTurnButton = new Button{
      text = "Prossimo Turno"
      onMouseClicked = event => GameController.nextTurn()
    }
    val box: HBox = new HBox {
      children = Seq(playerBox, nextTurnButton)
      spacing = 5
    }
    HBox.setHgrow(playerBox, Always)
    playerBox.maxWidth(Double.MaxValue)
    box

  private def nonActivePlayersPane(): Node =
    val nonActivePlayerDirectors = GameController.nonActivePlayerList.map(playerDirectors(_))
    val playerBoxes: Seq[Node] = nonActivePlayerDirectors
        .map(_.nonActivePlayerBox.create)
    val pane: HBox = new HBox {
      children = playerBoxes
      spacing = 5
    }
    pane