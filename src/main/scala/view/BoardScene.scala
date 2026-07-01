package view

import scalafx.scene.{Node, Scene}
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.Priority.Always
import scalafx.scene.layout.{BorderPane, FlowPane, HBox}
import scalafx.scene.paint.Color
import view.builders.{PlayerBoxBuilder, PlayerDirector}

class BoardScene extends Scene:
  private val director: PlayerDirector = PlayerDirector("name", Color.Green)

  root = new BorderPane {
    top = Label("top")
    center = Label("center")
    bottom = activePlayerPane()
  }

  private def activePlayerPane(): Node =
    val playerBoxBuilder = PlayerBoxBuilder()
    director.createActivePlayerBox(playerBoxBuilder)
    val playerBox = playerBoxBuilder.node
    val nextTurnButton = Button("Next Turn")
    val box: HBox = new HBox {
      children = Seq(playerBox, nextTurnButton)
      spacing = 5
    }
    HBox.setHgrow(playerBox, Always)
    playerBox.maxWidth(Double.MaxValue)
    box