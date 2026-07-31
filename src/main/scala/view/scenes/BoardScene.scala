package view.scenes

import controller.ViewPublishers.Context.{ActionContext, TurnChangeContext}
import controller.ViewPublishers.{ViewPublisher, ViewSubscriber}
import controller.ViewState.MatchEnd
import controller.dto.PlayerDTO
import controller.{ControllerStage, GameController, Navigator, ViewPublishers}
import scalafx.beans.property.{BooleanProperty, ObjectProperty}
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.Priority.Always
import scalafx.scene.layout.{BorderPane, FlowPane, HBox, VBox}
import scalafx.scene.{Node, Scene}
import view.LanguageStrings.BoardScreenStrings as BSStrings
import view.ViewComponents.ViewScene
import view.builders.PlayerGUIComponentFactory
import view.buttons.ButtonFactory
import view.panes.MissionPanes.{MissionBoardPane, ObtainedMissionPane}

class BoardScene(controller: GameController, controllerStage: ControllerStage) extends ViewScene[Node] with ViewSubscriber:
  private enum CentralPaneStates:
    case Missions
    case ObtainedMissions
    case Shop //TODO

  import CentralPaneStates.*
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

  private val missionPane = MissionBoardPane(controller.missions).pane
  private val centralPane: ObjectProperty[CentralPaneStates] = ObjectProperty(Missions)
  centralPane.onChange((_, _, newVal) =>
    pane.center = newVal match
      case Missions => missionPane
      case ObtainedMissions => obtainedMissionsPane
      case Shop => ???
  )

  private var turnPhaseSection: Node = Label(BSStrings.actionNotTakenText)

  private val actionTaken: BooleanProperty = BooleanProperty(false)
  actionTaken.onChange((_, _, newVal) =>
    turnPhaseSection = Label(if newVal then BSStrings.actionTakenText else BSStrings.actionNotTakenText)
    pane.bottom = activePlayerPane()
  )


  private val pane = new BorderPane {
    top = topMainPane()
    center = missionPane
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
    obtainedMissionsButton,
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
  private def obtainedMissionsButton: Node = ButtonFactory.makeBoardButton(
    "SEE MISSIONS",
    () => centralPane() = ObtainedMissions,
  )

  private def obtainedMissionsPane: Node = new FlowPane {
    children = controller.playerMissions(activePlayer()).map(ObtainedMissionPane(_))
  }

  private def roundCounter(): Node = HBox(Label(s"${controller.currentRound}/${controller.maxNumberOfRounds}"))

  override def scene: Node = pane

  override def update(context: ViewPublishers.Context): Unit = context match
    case TurnChangeContext =>
      activePlayer() = controller.activePlayer
      actionTaken() = controller.hasTurnActionBeenTaken
    case ActionContext => actionTaken() = controller.hasTurnActionBeenTaken
    case _ =>