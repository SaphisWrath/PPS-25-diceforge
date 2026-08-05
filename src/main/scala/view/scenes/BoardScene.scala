package view.scenes

import controller.ViewPublisher.ViewContext.{ResourceContext, TurnChangeContext, TurnStepChangeContext}
import controller.ViewPublisher.{ViewContext, ViewSubscriber}
import controller.dto.{EffectDTO, PlayerDTO}
import controller.{ControllerStage, GameController, PlayerChoice, ViewPublisher}
import model.Players.Player
import model.dice.Die
import model.effects.{Effect, OptionEffect}
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.scene.control.Label
import scalafx.scene.layout.Priority.Always
import scalafx.scene.layout.{BorderPane, FlowPane, HBox, VBox}
import scalafx.scene.{Group, Node}
import view.LanguageStrings.BoardScreenStrings as BSStrings
import view.ViewComponents.ViewScene
import view.builders.PlayerGUIComponentFactory
import view.buttons.ButtonFactory
import view.panes.ChoiceWindowChain
import view.panes.EffectPanes.{EffectPane, EffectWrapperPane}
import view.panes.MissionPanes.{MissionBoardPane, ObtainedMissionPane}
import view.panes.MultiPanes.{MultiPane, MultiPaneState}
import view.scenes.CentralPaneStates.ObtainedMissions
import view.theme.JfxTheme
import view.{Redrawable, scenes}

object CentralPaneStates:
  val Missions = MultiPaneState("Missions")
  val ObtainedMissions = MultiPaneState("ObtainedMissions")
  val Shop = MultiPaneState("Shop")

class BoardScene(controller: GameController, controllerStage: ControllerStage) extends ViewScene[Node] with ViewSubscriber:
  this.setPublisher(ViewPublisher())

  import CentralPaneStates.*

  private val playerDirectors: Map[PlayerDTO, PlayerGUIComponentFactory] =
    controller.players.map(p => p -> PlayerGUIComponentFactory(p, controller.playerBoard(p))).toMap
  private val activePlayerPropertyName = "activePlayer"
  private val activePlayer: ObjectProperty[PlayerDTO] = new ObjectProperty(this, activePlayerPropertyName, controller.activePlayer) {
    onChange((_, _, _) =>
      topMainPane.redraw()
      activePlayerPane.redraw()
      centralPane.setState(Missions)
      turnPhaseSection.redraw()
      obtainedMissionsButton.redraw()
    )
  }

  private val turnStep: StringProperty = StringProperty("")
  turnStep.onChange((_, _, _) =>
    centralPane.setState(if controller.canEndSupportPhase then ObtainedMissions else Missions)
    turnPhaseSection.redraw()
    obtainedMissionsPane.redraw()
    obtainedMissionsButton.redraw()
  )

  private val turnPhaseSection: Redrawable = Redrawable { () =>
    Label(turnStep())
  }

  private val centralPane: MultiPane = MultiPane(
    {
      case Missions => MissionBoardPane(controller.missions)
      case ObtainedMissions => obtainedMissionsPane()
    },
    Set(Missions, ObtainedMissions)
  )

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

  private def nonActivePlayersPane: Node =
    val nonActivePlayerDirectors = controller.nonActivePlayerList.map(playerDirectors(_))
    val playerBoxes: Seq[Node] = nonActivePlayerDirectors
      .map(_.nonActivePlayerBox)
    val pane: HBox = new HBox {
      children = playerBoxes
      spacing = 5
    }
    pane

  private val topMainPane: Redrawable = Redrawable { () =>
    new BorderPane {
      left = nonActivePlayersPane
      right = roundCounter
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
    () => !controller.canBuyExtraAction
  )

  private def throwDice(dice: Seq[(Player, Seq[Die])]): Unit =
    val diceThrowManager = controller.diceThrowManager
    manageChoices(diceThrowManager.copyEffectsFromRoll(dice), solvedCopyEffects =>
      manageChoices(diceThrowManager.optionEffectsFromRoll(solvedCopyEffects), solvedOptionEffects =>
        diceThrowManager.endRoll(solvedOptionEffects)
        this.mainPane.left = null
        ViewPublisher().notify(ResourceContext)
        controller.endDiceThrow()
      )
    )

  private def manageChoices[A](choices: Seq[PlayerChoice[A]], orElse: Seq[(Player, A)] => Unit): Unit =
    def nextChoiceWindow(results: Seq[(Player, A)], playerChoices: Seq[PlayerChoice[A]]): Unit =
      val popup = ChoiceWindowChain(playerChoices, results, nextChoiceWindow, orElse)
      popup.show({
        case effect: OptionEffect => EffectWrapperPane("", EffectDTO(effect), JfxTheme.primaryBorder)
        case effect: Effect => EffectPane(EffectDTO(effect))
        case _ => throw IllegalStateException("Choice element is not an effect")
      })
      if !popup.buttonsAvailable then popup.forceNext()

    if choices.isEmpty
    then orElse(Seq.empty)
    else nextChoiceWindow(Seq.empty, choices)

  private val obtainedMissionsPane: Redrawable = Redrawable { () =>
    new VBox {
      children = Seq(
        new FlowPane {
          children = controller.playerMissions(activePlayer()).map(ObtainedMissionPane(_))
        },
        if controller.canEndSupportPhase then
          ButtonFactory.makeBoardButton(BSStrings.endSupportPhaseButton, () => controller.endSupportPhase())
        else
          Group()
      )
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
            obtainedMissionsPane.redraw()
            obtainedMissionsButton.redraw()
        )
    }
  }

  private def roundCounter: Node = HBox(Label(s"${controller.currentRound}/${controller.maxNumberOfRounds}"))

  private val mainPane = new BorderPane {
    top = topMainPane.component
    center = centralPane.component
    bottom = activePlayerPane.component
  }

  override def scene: Node =
    centralPane.setState(ObtainedMissions)
    controller.startGame()
    mainPane

  override def update(context: ViewContext): Unit = context match
    case TurnChangeContext =>
      activePlayer() = controller.activePlayer
      throwDice(controller.players.map(p => (p.toPlayer, controller.playerDice(p))))
    case TurnStepChangeContext => turnStep() = controller.turnStep
    case _ =>