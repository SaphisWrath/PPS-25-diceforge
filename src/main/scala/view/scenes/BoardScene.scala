package view.scenes

import controller.ViewPublishers.Context.ResourceContext
import controller.PlayerChoice
import controller.dto.EffectDTO
import model.Players.Player
import model.effects.Effect
import model.utils.TemporaryDie
import controller.ViewPublishers.Context.{ActionContext, TurnChangeContext}
import controller.ViewPublishers.{ViewPublisher, ViewSubscriber}
import controller.dto.PlayerDTO
import controller.{ControllerStage, GameController, ViewPublishers}
import scalafx.beans.property.{BooleanProperty, ObjectProperty}
import scalafx.scene.control.Label
import scalafx.scene.layout.Priority.Always
import scalafx.scene.Node
import scalafx.scene.layout.{BorderPane, FlowPane, HBox, VBox}
import view.LanguageStrings.BoardScreenStrings as BSStrings
import view.ViewComponents.ViewScene
import view.builders.PlayerGUIComponentFactory
import view.buttons.ButtonFactory
import view.panes.MissionPanes.ObtainedMissionPane
import view.panes.MultiPanes.{MultiPane, MultiPaneState}
import view.scenes.CentralPaneStates.ObtainedMissions
import view.{Redrawable, scenes}
import view.panes.ChoiceWindowChain
import view.panes.EffectPanes.EffectPane
import view.panes.MissionPanes.MissionBoardPane

object CentralPaneStates:
  val Missions = MultiPaneState("Missions")
  val ObtainedMissions = MultiPaneState("ObtainedMissions")
  val Shop = MultiPaneState("Shop")

class BoardScene(controller: GameController, controllerStage: ControllerStage) extends ViewScene[Node] with ViewSubscriber:
  this.setPublisher(ViewPublisher)

  import CentralPaneStates.*

  private val playerDirectors: Map[PlayerDTO, PlayerGUIComponentFactory] =
    controller.players.map(p => p -> PlayerGUIComponentFactory(p, controller.playerBoard(p))).toMap
  private val activePlayerPropertyName = "activePlayer"
  private val activePlayer: ObjectProperty[PlayerDTO] = new ObjectProperty(this, activePlayerPropertyName, controller.activePlayer) {
    onChange((_, _, _) =>
      topMainPane.redraw()
      centralPane.setState(Missions)
      obtainedMissionsButton.redraw()
    )
  }
  private val actionTaken: BooleanProperty = BooleanProperty(false)
  actionTaken.onChange((_, _, newVal) =>
    turnPhaseSection.redraw()
  )
  private val turnPhaseSection: Redrawable = Redrawable { () =>
    Label(if actionTaken() then BSStrings.actionTakenText else BSStrings.actionNotTakenText)
  }
  private val centralPane: MultiPane = MultiPane(
    {
      case Missions => MissionBoardPane(controller.missions).pane
      case ObtainedMissions => obtainedMissionsPane()
    },
    Set(Missions, ObtainedMissions)
  )
  centralPane.setState(Missions)

  private val activePlayerPane: Redrawable = Redrawable { () =>
    val playerBox = playerDirectors(activePlayer()).activePlayerBox
    val playerPane: HBox = new HBox {
      children = Seq(playerBox, menuSection)
      spacing = 5
    }
    HBox.setHgrow(playerBox, Always)
    playerBox.maxWidth(Double.MaxValue)
    playerPane
  }

  private val nonActivePlayersPane: Redrawable = Redrawable { () =>
    val nonActivePlayerDirectors = controller.nonActivePlayerList.map(playerDirectors(_))
    val playerBoxes: Seq[Node] = nonActivePlayerDirectors
      .map(_.nonActivePlayerBox)
    val pane: HBox = new HBox {
      children = playerBoxes
      spacing = 5
    }
    pane
  }

  private val topMainPane: Redrawable = Redrawable { () =>
    new BorderPane {
      left = nonActivePlayersPane()
      right = roundCounter()
    }
  }

  private def menuSection: Node = VBox(
    turnPhaseSection(),
    buyExtraActionButton,
    nextTurnButton,
    obtainedMissionsButton(),
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
        this.mainPane.left = null
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
      this.mainPane.left = popup.pane
      if !popup.buttonsAvailable then popup.forceNext()

    if choices.isEmpty
      then orElse(Seq.empty)
    else fun(Seq.empty, choices)

  private val obtainedMissionsPane: Redrawable = Redrawable { () =>
    new FlowPane {
      children = controller.playerMissions(activePlayer()).map(ObtainedMissionPane(_))
    }
  }

  private val obtainedMissionsButton: Redrawable = Redrawable { () =>
    new FlowPane {
      children = centralPane.currentState match
        case ObtainedMissions => ButtonFactory.makeBoardButton(
          BSStrings.hideObtainedMissionsButton,
          () =>
            centralPane.setState(Missions)
            obtainedMissionsButton.redraw()
        )
        case _ => ButtonFactory.makeBoardButton(
          BSStrings.showObtainedMissionsButton,
          () =>
            centralPane.setState(ObtainedMissions)
            obtainedMissionsButton.redraw()
        )
    }
  }

  private def roundCounter(): Node = HBox(Label(s"${controller.currentRound}/${controller.maxNumberOfRounds}"))

  private val mainPane = new BorderPane {
    top = topMainPane.component
    center = centralPane.component
    bottom = activePlayerPane.component
  }

  override def scene: Node = mainPane

  override def update(context: ViewPublishers.Context): Unit = context match
    case TurnChangeContext =>
      activePlayer() = controller.activePlayer
      actionTaken() = controller.hasTurnActionBeenTaken
      throwDice(controller.players.map(p => (p.toPlayer, controller.playerDice(p))))
    case ActionContext => actionTaken() = controller.hasTurnActionBeenTaken
    case _ =>