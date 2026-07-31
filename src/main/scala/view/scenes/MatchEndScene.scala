package view.scenes

import controller.ViewState.{MainMenu, MatchInit}
import controller.{ControllerMatchEnd, ControllerStage}
import model.Players.Player
import model.resource.GloryPoint
import scalafx.geometry.Insets
import scalafx.geometry.Pos.Center
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.*
import scalafx.scene.paint.Color
import scalafx.scene.text.Font
import scalafx.scene.{Node, Scene}
import view.LanguageStrings.{EndScreenStrings as ESStrings, ResourceStrings as RStrings}
import view.ViewComponents.ViewScene

class MatchEndScene(controller: ControllerMatchEnd, controllerStage: ControllerStage) extends ViewScene[Node]:
  private val newMatchButton = new Button {
    text = ESStrings.playAgainButtonText
    onAction = _ => controllerStage.changeScene(MatchInit)
  }
  private val endGameButton = new Button {
    text = ESStrings.exitButtonText
    onAction = _ => controllerStage.changeScene(MainMenu)
  }

  private def makeRowWith(nodes: Iterable[Node]): HBox =
    new HBox {
      spacing = 20
      alignment = Center
      alignmentInParent = Center
      children = nodes
    }

  private def setupPlayerRanking(players: Seq[(Player, GloryPoint)]): Seq[HBox] = {
    var labelSizeX = 200
    var labelSizeY = 100
    var fontSize = 20

    players.map((player, points) =>
      val nameLabel = new Label(player.name)
      val pointLabel = new Label(s"${points.amount} ${RStrings.gloryPoint}")

      nameLabel.setMinSize(labelSizeX, labelSizeY)
      pointLabel.setMinSize(labelSizeX, labelSizeY)
      nameLabel.font = new Font(fontSize)
      pointLabel.font = new Font(fontSize)
      nameLabel.alignment = Center
      nameLabel.alignmentInParent = Center
      nameLabel.textFill = Color.White
      nameLabel.background = new Background(Array(new BackgroundFill(
        Color.valueOf(player.color.toString),
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
