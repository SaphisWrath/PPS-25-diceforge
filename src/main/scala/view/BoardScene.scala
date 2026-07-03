package view

import scalafx.scene.{Node, Scene}
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.Priority.Always
import scalafx.scene.layout.{BorderPane, FlowPane, HBox}
import scalafx.scene.paint.Color
import view.builders.PlayerDirector

class BoardScene extends Scene:
  private val director: PlayerDirector = PlayerDirector("name", Color.Green)

  root = new BorderPane {
    top = nonActivePlayersPane()
    center = Label("center")
    bottom = activePlayerPane()
  }

  private def activePlayerPane(): Node =
    val playerBox = director.activePlayerBox.create
    val nextTurnButton = Button("Next Turn")
    val box: HBox = new HBox {
      children = Seq(playerBox, nextTurnButton)
      spacing = 5
    }
    HBox.setHgrow(playerBox, Always)
    playerBox.maxWidth(Double.MaxValue)
    box

  private def nonActivePlayersPane(): Node =

    val nonActivePlayerDirectors = Seq(
        ("Player2", Color.Blue),
        ("Player3", Color.Orange),
        ("Player4", Color.Black)
      ).map(t => PlayerDirector(t._1, t._2))
    val playerBoxes: Seq[Node] = nonActivePlayerDirectors
        .map(_.activePlayerBox.create)
    val pane: HBox = new HBox {
      children = playerBoxes
      spacing = 5
    }

    pane