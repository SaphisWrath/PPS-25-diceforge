package controller

import controller.ViewState.*
import model.ModelPublisher

enum ViewState:
  case MainMenu
  case MatchInit
  case Board
  case MatchEnd

trait ControllerStage:
  /** Initialize the Stage
   *
   * This method should be called before any use of this class.
   * Set the ViewState to the initial one.
   */
  def init(): Unit

  /** Change the view state
   *
   * Set the view state to [[newState]] and change the scene accordingly
   *
   * @param newState The state that should be set
   */
  def changeScene(newState: ViewState): Unit

  /** Gives access to the current state
   *
   * @return the current [[ViewState]]
   */
  def currentViewState: ViewState

object ControllerStage:
  private class ControllerStageImpl(private val navigator: Navigator, startingState: ViewState) extends ControllerStage:
    private var viewState: ViewState = startingState

    override def init(): Unit = changeScene(viewState)

    override def changeScene(newState: ViewState): Unit =
      resetPublishers()
      newState match
        case MainMenu => navigator.navigateToMainMenu()
        case MatchInit => navigator.navigateToMatchInit()
        case Board => navigator.navigateToBoard()
        case MatchEnd => navigator.navigateToMatchEnd()
      viewState = newState

    private def resetPublishers(): Unit =
      ViewPublisher().reset()
      ModelPublisher().reset()

    override def currentViewState: ViewState = viewState

  def apply(navigator: Navigator, startingState: ViewState): ControllerStage = ControllerStageImpl(navigator, startingState)
