package view.scenes

import controller.GameController
import controller.dto.PlayerDTO
import scalafx.beans.property.ObjectProperty
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.Priority.Always
import scalafx.scene.layout.{BorderPane, HBox}
import scalafx.scene.{Node, Scene}
import view.builders.PlayerGUIComponentFactory
import view.buttons.ButtonFactory
import view.panes.{MissionBoardPane, MissionPane}
import view.LanguageStrings.BoardScreenStrings as BSStrings
import view.ViewComponents.ViewScene

class BoardScene extends ViewScene[Node]:
  private val playerDirectors: Map[PlayerDTO, PlayerGUIComponentFactory] =
    GameController.players.map(p => p -> PlayerGUIComponentFactory(p, GameController.playerBoard(p))).toMap

  private val activePlayerPropertyName = "activePlayer"
  private val activePlayer: ObjectProperty[PlayerDTO] = new ObjectProperty(this, activePlayerPropertyName, GameController.activePlayer.get) {
    onChange((_, _, _) =>
      pane.top = nonActivePlayersPane()
      pane.bottom = activePlayerPane()
    )
  }

  private val pane = new BorderPane {
    top = nonActivePlayersPane()
    center = MissionBoardPane(GameController.missions).component
    bottom = activePlayerPane()
  }

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

  private def nextTurnButton: Button = ButtonFactory.makeBoardButton(
    BSStrings.nextTurnButtonText,
    event =>
      GameController.nextTurn()
      activePlayer() = GameController.activePlayer.get
  )

  override def scene: Node = pane