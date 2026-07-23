package controller

import controller.ViewState.*

enum ViewState:
  case MainMenu
  case MatchInit
  case Board
  case MatchEnd

trait ControllerStage:
  def init(navigator: Navigator): Unit
  def changeScene(newState: ViewState): Unit
  def getViewState: ViewState

object ControllerStage extends ControllerStage:
  private var viewState: ViewState = MainMenu
  private var _navigator: Option[Navigator] = Option.empty
  private def navigator: Navigator = _navigator match
    case Some(value) => value
    case _ => throw IllegalStateException("Controller not Initialized")

  override def init(navigator: Navigator): Unit =
    _navigator = Option(navigator)
    changeScene(MainMenu)

  override def changeScene(newState: ViewState): Unit =
    newState match
      case MainMenu => navigator.navigateToMainMenu()
      case MatchInit => navigator.navigateToMatchInit()
      case Board => navigator.navigateToBoard()
      case MatchEnd => navigator.navigateToMatchEnd()
    viewState = newState
  override def getViewState: ViewState = viewState
