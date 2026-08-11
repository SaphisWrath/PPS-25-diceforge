package controller

import model.{GameMatch, MatchBuilder, MatchBuilderImpl}
import view.ViewComponents.{MainStage, ViewSceneFactory}

trait ControllerManager:
  /** Creates the controller of the game
   *
   * Gives access to every information useful for the game representation and management
   *
   * @return The corresponding [[GameController]] instance
   */
  def gameController: GameController

  /** Creates the controller of the stage
   *
   * Gives access to the state of the view and to the method necessary to change it
   *
   * @return The corresponding [[ControllerStage]] instance
   */
  def stageController: ControllerStage[StandardViewState]

  /** Creates the controller responsible for the game initialization
   *
   * Gives access to the methods used for the game creation
   *
   * @return The corresponding [[ControllerMatchInit]] instance
   */
  def matchInitController: ControllerMatchInit

  /** Creates the controller used to represent an ended game
   *
   * Gives access to the method used to represent the stats of an ended Game
   *
   * @return The corresponding [[ControllerMatchEnd]] instance
   */
  def matchEndController: ControllerMatchEnd

object ControllerManager:
  private class ControllerManagerImpl[T](
                                          mainStageProducer: () => MainStage[T],
                                          viewSceneFactoryProducer: ControllerManager => ViewSceneFactory[T, StandardViewState]
                                        ) extends ControllerManager:
    private var gameMatch: Option[GameMatch] = Option.empty
    override val stageController: ControllerStage[StandardViewState] =
      ControllerStage(Navigator(mainStageProducer(), viewSceneFactoryProducer(this)), StandardViewState.MainMenu)

    override val matchInitController: ControllerMatchInit = ControllerMatchInit()

    override def gameController: GameController =
      gameMatch = Option(matchInitController.builder.build())
      matchInitController.reset()
      GameController(gameMatch.get)

    override def matchEndController: ControllerMatchEnd = gameMatch match
      case Some(gM) => ControllerMatchEnd(gM)
      case _ => throw IllegalStateException("No game match has been initialized")

  def apply[T](
                mainStageProducer: () => MainStage[T],
                viewSceneFactoryProducer: ControllerManager => ViewSceneFactory[T, StandardViewState]
              ): ControllerManager = ControllerManagerImpl[T](mainStageProducer, viewSceneFactoryProducer)
