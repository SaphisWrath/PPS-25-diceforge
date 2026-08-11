package controller

import view.ViewComponents.{MainStage, ViewSceneFactory}

trait ViewState

trait Navigator[VS]:
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
