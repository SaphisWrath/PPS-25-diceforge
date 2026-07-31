package controller

import view.ViewComponents.{MainStage, ViewSceneFactory}

trait Navigator:
  def navigateToMainMenu(): Unit

  def navigateToMatchInit(): Unit

  def navigateToBoard(): Unit

  def navigateToMatchEnd(): Unit

object Navigator:
  private class NavigatorImpl[T](mainStage: MainStage[T], viewSceneFactory: ViewSceneFactory[T]) extends Navigator:

    override def navigateToMainMenu(): Unit = mainStage.setContent(viewSceneFactory.createMainMenuScene())

    override def navigateToMatchInit(): Unit = mainStage.setContent(viewSceneFactory.createMatchInitScene())

    override def navigateToBoard(): Unit = mainStage.setContent(viewSceneFactory.createBoardScene())

    override def navigateToMatchEnd(): Unit = mainStage.setContent(viewSceneFactory.createMatchEndScene())

  def apply[T](mainStage: MainStage[T], viewSceneFactory: ViewSceneFactory[T]): Navigator =
    NavigatorImpl(mainStage, viewSceneFactory)
