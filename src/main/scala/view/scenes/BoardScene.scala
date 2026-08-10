package view.scenes

import controller.ViewPublisher.ViewContext.{ItemObtainedContext, MissionBoughtContext, PlayerChoiceContext, PlayerMovedContext, ResourceContext, SelectDieForThrowContext, TurnChangeContext, TurnStepChangeContext}
import controller.ViewPublisher.{ViewContext, ViewSubscriber}
import controller.ViewState.MatchEnd
import controller.dto.{DieDTO, EffectDTO, PlayerDTO}
import controller.{ControllerStage, GameController, ViewPublisher}
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.scene.control.Label
import scalafx.scene.layout.Priority.Always
import scalafx.scene.layout.{BorderPane, FlowPane, HBox, VBox}
import scalafx.scene.{Group, Node}
import view.LanguageStrings.BoardScreenStrings as BSStrings
import view.ViewComponents.ViewScene
import view.builders.PlayerGUIComponentFactory
import view.buttons.ButtonFactory
import view.panes.ChoiceWindowChain.manageChoices
import view.panes.DicePanes.DiePane
import view.panes.EffectPanes.effectPane
import view.panes.MissionPanes.{MissionBoardPane, ObtainedMissionPane}
import view.panes.MultiPanes.{MultiPane, MultiPaneState}
import view.panes.ShopPanes.ShopPane
import view.scenes.CentralPaneStates.ObtainedMissions
import view.{Redrawable, scenes}

object CentralPaneStates:
  val Start = MultiPaneState("Start")
  val Missions = MultiPaneState("Missions")
  val ObtainedMissions = MultiPaneState("ObtainedMissions")
  val Shop = MultiPaneState("Shop")

class BoardScene(controller: GameController, controllerStage: ControllerStage) extends ViewScene[Node] with ViewSubscriber:
  this.setPublisher(ViewPublisher())

  import CentralPaneStates.*

  private val playerFactories: Map[PlayerDTO, PlayerGUIComponentFactory] =
    controller.players.map(p => p -> PlayerGUIComponentFactory(
      p,
      controller.playerBoard(p),
      () => controller.recentDiceResults(p.name)
    )).toMap
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
    centralPane.setState(if controller.isSupportPhase then ObtainedMissions else Missions)
    turnPhaseSection.redraw()
    obtainedMissionsPane.redraw()
    obtainedMissionsButton.redraw()
    visitShopButton.redraw()
  )

  private val turnPhaseSection: Redrawable = Redrawable { () =>
    Label(turnStep())
  }

  private val missionPane: Redrawable = Redrawable{ () =>
    MissionBoardPane(
      controller.missions,
      controller.playerPositions.map((i, p) => (i, playerFactories(p).onlyToken))
    )
  }

  private val shopPane: Redrawable = Redrawable { () =>
    ShopPane(controller.shopItems)
  }

  private val centralPane: MultiPane = MultiPane(
    {
      case Start => startPane
      case Missions => missionPane()
      case ObtainedMissions => obtainedMissionsPane()
      case Shop => shopPane()
    },
    Set(Start, Missions, ObtainedMissions, Shop)
  )

  private def startPane: Node = new BorderPane {
    center = ButtonFactory.makeBoardButton(BSStrings.startButtonText, () => controller.startGame())
  }

  private val activePlayerPane: Redrawable = Redrawable { () =>
    val playerBox = playerFactories(activePlayer()).activePlayerBox
    val playerPane: HBox = new HBox {
      children = Seq(playerBox, menuSection)
      spacing = 5
    }
    HBox.setHgrow(playerBox, Always)
    playerBox.maxWidth(Double.MaxValue)
    playerPane
  }

  private def nonActivePlayersPane: Node =
    val nonActivePlayerDirectors = controller.nonActivePlayerList.map(playerFactories(_))
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
    visitShopButton(),
    nextTurnButton,
    obtainedMissionsButton()
  )

  private val nextTurnButton: Node = ButtonFactory.makeBoardButton(
    BSStrings.nextTurnButtonText,
    () => controller.nextTurn(),
    () => !controller.canGoToNextTurn || centralPane.currentState == Start
  )

  private val buyExtraActionButton: Node = ButtonFactory.makeBoardButton(
    BSStrings.buyExtraActionButton,
    () => controller.buyExtraAction(),
    () => !controller.canBuyExtraAction
  )

  private val visitShopButton: Redrawable = Redrawable { () =>
    new FlowPane {
      children = centralPane.currentState match
        case Shop => ButtonFactory.makeBoardButton(
          BSStrings.leaveShopButton,
          () =>
            centralPane.setState(Missions)
            visitShopButton.redraw(),
          () => controller.canTakeAction
        )
        case _ => ButtonFactory.makeBoardButton(
          BSStrings.visitShopButton,
          () =>
            centralPane.setState(Shop)
            visitShopButton.redraw(),
          () => controller.canTakeAction
        )
    }
  }

  private val obtainedMissionsPane: Redrawable = Redrawable { () =>
    new VBox {
      children = Seq(
        new FlowPane {
          children = controller.playerMissions(activePlayer()).map(ObtainedMissionPane(_))
        },
        if controller.isSupportPhase then
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
            obtainedMissionsButton.redraw(),
          () => !controller.canGoToNextTurn
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
    centralPane.setState(Start)
    mainPane

  override def update(context: ViewContext): Unit = context match
    case TurnChangeContext =>
      if controller.isGameEnded
      then controllerStage.changeScene(MatchEnd)
      else activePlayer() = controller.activePlayer
    case TurnStepChangeContext => turnStep() = controller.turnStep
    case PlayerMovedContext => missionPane.redraw()
    case PlayerChoiceContext =>
      val choiceController = controller.solveController
      manageChoices[EffectDTO](choiceController.pendingChoices, choiceController.resumeAfterChoices, effectPane(_))
    case ItemObtainedContext =>
      shopPane.redraw()
      manageChoices[DieDTO](
        Seq((controller.activePlayer, controller.dice(controller.activePlayer))),
        results => {
          val faceSwapController = controller.faceSwapController(results.head)
          manageChoices[EffectDTO](
            faceSwapController.pendingChoices,
            faceSwapController.resumeAfterChoices,
            effectPane(_)
          )
        },
        DiePane(_)
      )
    case SelectDieForThrowContext =>
      val dieRollController = controller.dieChoiceAndRollController
      manageChoices[DieDTO](
        dieRollController.pendingChoices,
        dieRollController.resumeAfterChoices,
        DiePane(_)
      )
    case PlayerMovedContext | MissionBoughtContext => missionPane.redraw()
    case _ =>