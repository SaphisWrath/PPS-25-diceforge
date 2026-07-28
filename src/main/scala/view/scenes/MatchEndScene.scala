package view.scenes

import controller.ControllerMatchEnd
import model.Players.Player
import model.resource.GloryPoint
import scalafx.geometry.Insets
import scalafx.geometry.Pos.Center
import scalafx.scene.{Node, Scene}
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.*
import scalafx.scene.paint.Color
import scalafx.scene.text.Font
import view.LanguageStrings.{EndScreenStrings as ESStrings, ResourceStrings as RStrings}
import view.ViewComponents.ViewScene

class MatchEndScene(controller: ControllerMatchEnd) extends ViewScene[Node]:
  private val newMatchButton = new Button(ESStrings.playAgainButtonText)
  private val endGameButton = new Button(ESStrings.exitButtonText)

  private def makeRowWith(nodes: Iterable[Node]): HBox =
    new HBox {
      spacing = 20
      alignment = Center
      alignmentInParent = Center
      children = nodes
    }

  private def setupPlayerRanking(players: List[(Player, GloryPoint)]): Seq[HBox] = {
    var labelSizeX = 200
    var labelSizeY = 100
    var fontSize = 20

    players.map((player, points) =>
      val nameLabel = new Label(player.getName)
      val pointLabel = new Label(s"${points.amount} ${RStrings.gloryPoint}")

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

  override def scene: Node = new VBox {
    fillWidth = true
    spacing = 20
    alignment = Center
    alignmentInParent = Center
    val playerRanking: Seq[HBox] = setupPlayerRanking(controller.getSortedPlayers)
    children = playerRanking.appended(makeRowWith(Seq(newMatchButton, endGameButton)))
  }
