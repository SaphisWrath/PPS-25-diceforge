package view.scenes

import controller.ViewPublishers.ViewPublisher
import controller.ViewState.MatchEnd
import controller.{ControllerStage, GameController, Navigator, PlayerChoice}
import controller.dto.{EffectDTO, PlayerDTO}
import model.Players
import model.Players.Player
import model.dice.MockDieFactory.*
import model.effects.Effect
import model.utils.TemporaryDie
import scalafx.Includes.jfxNode2sfx
import scalafx.beans.property.ObjectProperty
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.Priority.Always
import scalafx.scene.layout.{BorderPane, HBox}
import scalafx.scene.{Node, Scene}
import view.builders.PlayerGUIComponentFactory
import view.buttons.ButtonFactory
import view.LanguageStrings.BoardScreenStrings as BSStrings
import view.ViewComponents.ViewScene
import view.panes.ChoiceWindow
import view.panes.MissionPanes.MissionBoardPane

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, blocking}

class BoardScene(controller: GameController, controllerStage: ControllerStage) extends ViewScene[Node]:
  private val playerDirectors: Map[PlayerDTO, PlayerGUIComponentFactory] =
    controller.players.map(p => p -> PlayerGUIComponentFactory(p, controller.playerBoard(p))).toMap

  private val activePlayerPropertyName = "activePlayer"
  private val activePlayer: ObjectProperty[PlayerDTO] = new ObjectProperty(this, activePlayerPropertyName, controller.activePlayer) {
    onChange((_, _, _) =>
      pane.top = topMainPane()
      pane.bottom = activePlayerPane()
    )
  }

  private val pane = new BorderPane {
    top = topMainPane()
    center = MissionBoardPane(controller.missions).pane
    bottom = activePlayerPane()
  }

  private def topMainPane(): Node = new BorderPane {
    left = nonActivePlayersPane()
    right = roundCounter()
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
    val nonActivePlayerDirectors = controller.nonActivePlayerList.map(playerDirectors(_))
    val playerBoxes: Seq[Node] = nonActivePlayerDirectors
      .map(_.nonActivePlayerBox)
    val pane: HBox = new HBox {
      children = playerBoxes
      spacing = 5
    }
    pane

  private def nextTurnButton: Button = ButtonFactory.makeBoardButton(
    BSStrings.nextTurnButtonText,
    () =>
      controller.nextTurn()
      if controller.isGameEnded then
        controllerStage.changeScene(MatchEnd)
      else
        activePlayer() = controller.activePlayer
        throwDice(controller.players.map(p => (p.toPlayer, controller.playerDice(p))))
  )

  private def throwDice(dice: Seq[(Player, Seq[TemporaryDie])]): Unit =
    val diceThrowManager = controller.diceThrowManager
    manageChoices(diceThrowManager.copyEffectsFromRoll(dice), solvedCopyEffects =>
      manageChoices(diceThrowManager.optionEffectsFromRoll(solvedCopyEffects), solvedOptionEffects =>
        diceThrowManager.endRoll(solvedOptionEffects)
        this.pane.left = null
        ViewPublisher.notifyResourceChange()
      )
    )

  private def manageChoices[A](choices: Seq[PlayerChoice[A]], orElse: Seq[(Player, A)] => Unit): Unit =
    def fun(results: Seq[(Player, A)], playerChoices: Seq[PlayerChoice[A]]): Unit =
      val popup = ChoiceWindow(playerChoices, results, fun, orElse)
      popup.stringSupplier_= {
        case effect: Effect => EffectDTO(effect).toString
        case _ => "idk"
      }
      this.pane.left = popup.pane

    if choices.isEmpty
      then orElse(Seq.empty)
    else fun(Seq.empty, choices)
  
  private def roundCounter(): Node = HBox(Label(s"${controller.currentRound}/${controller.maxNumberOfRounds}"))

  override def scene: Node = pane