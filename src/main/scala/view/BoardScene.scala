package view

import controller.GameController
import model.Players.Player
import scalafx.scene.{Node, Scene}
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.Priority.Always
import scalafx.scene.layout.{BorderPane, FlowPane, HBox}
import scalafx.scene.paint.Color
import view.builders.{PlayerBoxBuilder, PlayerDirector}
import view.utils.ColorConversion.*

class BoardScene extends Scene:
  private val playerDirectors: Map[Player, PlayerDirector] = 
    GameController.players.map(p => p -> PlayerDirector(p.getName, p.getColor.toScalaFX)).toMap

  root = new BorderPane {
    top = nonActivePlayersPane()
    center = Label("center")
    bottom = activePlayerPane()
  }

  private def activePlayerPane(): Node =
    val playerBoxBuilder = PlayerBoxBuilder.standardPlayerBoxBuilder
    val activePlayer = GameController.activePlayer.get
    playerDirectors(activePlayer).createActivePlayerBox(playerBoxBuilder)
    val playerBox = playerBoxBuilder.node
    val nextTurnButton = Button("Next Turn")
    val box: HBox = new HBox {
      children = Seq(playerBox, nextTurnButton)
      spacing = 5
    }
    HBox.setHgrow(playerBox, Always)
    playerBox.maxWidth(Double.MaxValue)
    box

  private def nonActivePlayersPane(): Node =
    val nonActivePlayerDirectors = GameController.nonActivePlayerList.map(playerDirectors(_))
    val builder: PlayerBoxBuilder = PlayerBoxBuilder.fillInPlayerBoxBuilder
    val playerBoxes: Seq[Node] = nonActivePlayerDirectors
        .map(director =>
          director.createActivePlayerBox(builder)
          builder.node
      )
    val pane: HBox = new HBox {
      children = playerBoxes
      spacing = 5
    }

    pane