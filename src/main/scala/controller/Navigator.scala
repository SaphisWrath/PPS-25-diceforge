package controller

import view.ViewComponents.{MainStage, ViewSceneFactory}

trait ViewState

/** Interface for handling the view transitions.
 *
 * @tparam VS The accepted type of [[ViewState]]
 */
trait Navigator[VS <: ViewState]:
  /** Set the View content to the new ViewState
   *
   * @param viewState The instance to set as the new viewState
   */
  def navigateTo(viewState: VS): Unit

object Navigator:
  private class NavigatorImpl[T, VS <: ViewState](mainStage: MainStage[T], viewSceneFactory: ViewSceneFactory[T, VS]) extends Navigator[VS]:

    override def navigateTo(viewState: VS): Unit = mainStage.setContent(viewSceneFactory.createScene(viewState))

  def apply[T, VS <: ViewState](mainStage: MainStage[T], viewSceneFactory: ViewSceneFactory[T, VS]): Navigator[VS] =
    NavigatorImpl(mainStage, viewSceneFactory)
