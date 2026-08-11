package controller

import view.ViewComponents.{MainStage, ViewSceneFactory}

trait Navigator:
  /**Set the View to show the main menu
   *
   */
  def navigateToMainMenu(): Unit

  /**Set the View to show the match init screen
   *
   */
  def navigateToMatchInit(): Unit

  /**Set the View to show the gameboard
   *
   */
  def navigateToBoard(): Unit

  /**Set the View to show the match end screen
   *
   */
  def navigateToMatchEnd(): Unit

object Navigator:
  private class NavigatorImpl[T](mainStage: MainStage[T], viewSceneFactory: ViewSceneFactory[T]) extends Navigator:

    override def navigateToMainMenu(): Unit = mainStage.setContent(viewSceneFactory.createMainMenuScene())

    override def navigateToMatchInit(): Unit = mainStage.setContent(viewSceneFactory.createMatchInitScene())

    override def navigateToBoard(): Unit = mainStage.setContent(viewSceneFactory.createBoardScene())

    override def navigateToMatchEnd(): Unit = mainStage.setContent(viewSceneFactory.createMatchEndScene())

  def apply[T](mainStage: MainStage[T], viewSceneFactory: ViewSceneFactory[T]): Navigator =
    NavigatorImpl(mainStage, viewSceneFactory)
