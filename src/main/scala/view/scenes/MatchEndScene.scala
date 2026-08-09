package view.scenes

import controller.ViewState.{MainMenu, MatchInit}
import controller.dto.PlayerDTO
import controller.{ControllerMatchEnd, ControllerStage}
import scalafx.geometry.Insets
import scalafx.geometry.Pos.Center
import scalafx.scene.control.Label
import scalafx.scene.layout.*
import scalafx.scene.paint.Color
import scalafx.scene.text.Font
import scalafx.scene.Node
import view.LanguageStrings.{EndScreenStrings as ESStrings, ResourceStrings as RStrings}
import view.ViewComponents.ViewScene
import view.buttons.ButtonFactory.makeMenuButton

class MatchEndScene(controller: ControllerMatchEnd, controllerStage: ControllerStage) extends ViewScene[Node]:
  private val newMatchButton = makeMenuButton(
    ESStrings.playAgainButtonText,
    _ => controllerStage.changeScene(MatchInit)
  )
  private val endGameButton = makeMenuButton(
    ESStrings.exitButtonText,
    _ => controllerStage.changeScene(MainMenu)
  )

  private def makeRowWith(nodes: Iterable[Node]): HBox =
    new HBox {
      spacing = 20
      alignment = Center
      alignmentInParent = Center
      children = nodes
    }

  private def setupPlayerRanking(players: Seq[(PlayerDTO, Int)]): Seq[HBox] = {
    def samePlacement(player: (PlayerDTO, Int), previousPlayer: Option[(PlayerDTO, Int)]): Boolean =
      previousPlayer.isEmpty || previousPlayer.get._2 == player._2

    def buildLabel(_text: String, sizeX: Int, sizeY: Int, fontSize: Int): Label =
      new Label {
        text = _text
        font = new Font(fontSize)
        minWidth = sizeX
        minHeight = sizeY
      }

    var labelSizeX = 200
    var labelSizeY = 100
    var fontSize = 20
    var placement = 1
    val previousPlayers = Seq(Option.empty).concat(players.map(Option(_)).take(players.size - 1))

    players
      .zip(previousPlayers)
      .map((pair, prevPlayer) =>
        val (player, points) = pair
        if !samePlacement(pair, prevPlayer)
        then
          placement = players.map(_._1).indexOf(player) + 1
          labelSizeX = 100
          labelSizeY = 50
          fontSize = 15
        val placementLabel = buildLabel(s"$placement.", 20, labelSizeY, fontSize)
        val nameLabel = buildLabel(player.name, labelSizeX, labelSizeY, fontSize)
        val pointLabel = buildLabel(s"${points} ${RStrings.gloryPoint}", labelSizeX, labelSizeY, fontSize)

        nameLabel.alignment = Center
        nameLabel.alignmentInParent = Center
        nameLabel.textFill = Color.White
        nameLabel.background = new Background(Array(new BackgroundFill(
          Color.valueOf(player.colorHex),
          CornerRadii.Empty,
          Insets.Empty
        )))
        val returnBox = makeRowWith(Seq(placementLabel, nameLabel, pointLabel))
        returnBox.setMinSize(labelSizeX, labelSizeY)
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
