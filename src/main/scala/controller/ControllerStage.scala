package controller

import controller.StandardViewState.*
import controller.publishers.ViewPublisher
import model.ModelPublisher

enum StandardViewState extends ViewState:
  case MainMenu
  case MatchInit
  case Board
  case MatchEnd

trait ControllerStage[VS]:
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
  def changeScene(newState: VS): Unit

  /** Gives access to the current state
   *
   * @return the current [[VS]]
   */
  def currentViewState: VS

object ControllerStage:
  private class ControllerStageImpl[VS <: ViewState](private val navigator: Navigator[VS], startingState: VS) extends ControllerStage[VS]:
    private var viewState: VS = startingState

    override def init(): Unit = changeScene(viewState)

    override def changeScene(newState: VS): Unit =
      resetPublishers()
      navigator.navigateTo(newState)
      viewState = newState

    private def resetPublishers(): Unit =
      ViewPublisher().reset()
      ModelPublisher().reset()

    override def currentViewState: VS = viewState

  def apply[VS <: ViewState](navigator: Navigator[VS], startingState: VS): ControllerStage[VS] = ControllerStageImpl[VS](navigator, startingState)
