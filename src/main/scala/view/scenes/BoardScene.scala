package view.scenes

import controller.ViewPublishers.Context.{ActionContext, ResourceContext, TurnChangeContext}
import controller.ViewPublishers.{ViewPublisher, ViewSubscriber}
import controller.ViewPublishers.ViewPublisher
import controller.ViewState.MatchEnd
import controller.dto.PlayerDTO
import controller.{ControllerStage, GameController, Navigator, ViewPublishers}
import scalafx.beans.property.{BooleanProperty, ObjectProperty}
import controller.{ControllerStage, GameController, Navigator, PlayerChoice}
import controller.dto.{EffectDTO, PlayerDTO}
import model.Players.Player
import model.effects.{Effect, ResourceEffect}
import model.utils.TemporaryDie
import scalafx.beans.property.ObjectProperty
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.Priority.Always
import scalafx.scene.layout.{BorderPane, HBox, VBox}
import scalafx.scene.{Node, Scene}
import scalafx.scene.layout.{BorderPane, HBox}
import scalafx.scene.Node
import view.builders.PlayerGUIComponentFactory
import view.buttons.ButtonFactory
import view.LanguageStrings.BoardScreenStrings as BSStrings
import view.ViewComponents.ViewScene
import view.builders.PlayerGUIComponentFactory
import view.buttons.ButtonFactory
import view.panes.ChoiceWindowChain
import view.panes.EffectPanes.EffectPane
import view.panes.MissionPanes.MissionBoardPane

class BoardScene(controller: GameController, controllerStage: ControllerStage) extends ViewScene[Node] with ViewSubscriber:
  this.setPublisher(ViewPublisher)
  private val playerDirectors: Map[PlayerDTO, PlayerGUIComponentFactory] =
    controller.players.map(p => p -> PlayerGUIComponentFactory(p, controller.playerBoard(p))).toMap

  private val activePlayerPropertyName = "activePlayer"
  private val activePlayer: ObjectProperty[PlayerDTO] = new ObjectProperty(this, activePlayerPropertyName, controller.activePlayer) {
    onChange((_, _, _) =>
      pane.top = topMainPane()
      pane.bottom = activePlayerPane()
    )
  }

  private var turnPhaseSection: Node = Label(BSStrings.actionNotTakenText)

  private val actionTaken: BooleanProperty = BooleanProperty(false)
  actionTaken.onChange((_, _, newVal) =>
    turnPhaseSection = Label(if newVal then BSStrings.actionTakenText else BSStrings.actionNotTakenText)
    pane.bottom = activePlayerPane()
  )

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
      children = Seq(playerBox, menuSection)
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

  private def menuSection: Node = VBox(
    turnPhaseSection,
    buyExtraActionButton,
    nextTurnButton,
  )

  private def nextTurnButton: Node = ButtonFactory.makeBoardButton(
    BSStrings.nextTurnButtonText,
    () => controller.nextTurn()
  )

  private def buyExtraActionButton: Node = ButtonFactory.makeBoardButton(
    BSStrings.buyExtraActionButton,
    () => controller.buyExtraAction(),
    () => controller.hasExtraActionBeenBought
  )

  private def throwDice(dice: Seq[(Player, Seq[TemporaryDie])]): Unit =
    val diceThrowManager = controller.diceThrowManager
    manageChoices(diceThrowManager.copyEffectsFromRoll(dice), solvedCopyEffects =>
      manageChoices(diceThrowManager.optionEffectsFromRoll(solvedCopyEffects), solvedOptionEffects =>
        diceThrowManager.endRoll(solvedOptionEffects)
        this.pane.left = null
        ViewPublisher.notify(ResourceContext)
      )
    )

  private def manageChoices[A](choices: Seq[PlayerChoice[A]], orElse: Seq[(Player, A)] => Unit): Unit =
    def fun(results: Seq[(Player, A)], playerChoices: Seq[PlayerChoice[A]]): Unit =
      val popup = ChoiceWindowChain(playerChoices, results, fun, orElse)
      popup.setMapper {
        case effect: Effect => EffectPane(EffectDTO(effect))
        case _ => ???
      }
      this.pane.left = popup.pane

    if choices.isEmpty
      then orElse(Seq.empty)
    else fun(Seq.empty, choices)

  private def roundCounter(): Node = HBox(Label(s"${controller.currentRound}/${controller.maxNumberOfRounds}"))

  override def scene: Node = pane

  override def update(context: ViewPublishers.Context): Unit = context match
    case TurnChangeContext =>
      activePlayer() = controller.activePlayer
      actionTaken() = controller.hasTurnActionBeenTaken
      throwDice(controller.players.map(p => (p.toPlayer, controller.playerDice(p))))
    case ActionContext => actionTaken() = controller.hasTurnActionBeenTaken
    case _ =>