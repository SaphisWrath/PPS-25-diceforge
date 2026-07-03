package view.scenes

import controller.ControllerMatchEnd
import model.Players.Player
import model.resource.Resource
import model.resource.ResourceType.VictoryPoint
import scalafx.geometry.Insets
import scalafx.geometry.Pos.Center
import scalafx.scene.{Node, Scene}
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.*
import scalafx.scene.paint.Color
import scalafx.scene.text.Font

class MatchEndScene(controller: ControllerMatchEnd) extends Scene:
  private val newMatchButton = new Button("Play again")
  private val endGameButton = new Button("Quit")

  private def makeRowWith(nodes: Iterable[Node]): HBox =
    new HBox {
      spacing = 20
      alignment = Center
      alignmentInParent = Center
      children = nodes
    }

  private def setupPlayerRanking(players: List[(Player, Resource[VictoryPoint])]): Seq[HBox] = {
    var labelSizeX = 200
    var labelSizeY = 100
    var fontSize = 20

    players.map((player, points) =>
      val nameLabel = new Label(player.getName)
      val pointLabel = new Label(points.currentAmount + " Victory Points")

      nameLabel.setMinSize(labelSizeX, labelSizeY)
      pointLabel.setMinSize(labelSizeX, labelSizeY)
      nameLabel.font = new Font(fontSize)
      pointLabel.font = new Font(fontSize)
      nameLabel.alignment = Center
      nameLabel.alignmentInParent = Center
      nameLabel.textFill = Color.White
      nameLabel.background = new Background(Array(new BackgroundFill(
        Color.valueOf(player.getColor.toString),
        CornerRadii.Empty,
        Insets.Empty
      )))
      val returnBox = makeRowWith(Seq(nameLabel, pointLabel))
      returnBox.setMinSize(labelSizeX, labelSizeY)
      labelSizeX = 100
      labelSizeY = 50
      fontSize = 15
      returnBox
    )
  }

  root = new VBox {
    fillWidth = true
    spacing = 20
    alignment = Center
    alignmentInParent = Center
    val playerRanking: Seq[HBox] = setupPlayerRanking(controller.getSortedPlayers)
    children = playerRanking.appended(makeRowWith(Seq(newMatchButton, endGameButton)))
  }
