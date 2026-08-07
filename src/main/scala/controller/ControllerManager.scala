package controller

import model.GameMatch
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
  def stageController: ControllerStage

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
                                          viewSceneFactoryProducer: ControllerManager => ViewSceneFactory[T]
                                        ) extends ControllerManager:
    private var gameMatch: Option[GameMatch] = Option.empty
    private val matchBuilder: MatchBuilder = MatchBuilderImpl()
    override val stageController: ControllerStage =
      ControllerStage(Navigator(mainStageProducer(), viewSceneFactoryProducer(this)))

    override def matchInitController: ControllerMatchInit = ControllerMatchInit(matchBuilder)

    override def gameController: GameController =
      gameMatch = Option(matchBuilder.build())
      GameController(gameMatch.get)

    override def matchEndController: ControllerMatchEnd = ControllerMatchEnd(gameMatch.get)

  def apply[T](
                mainStageProducer: () => MainStage[T],
                viewSceneFactoryProducer: ControllerManager => ViewSceneFactory[T]
              ): ControllerManager = ControllerManagerImpl[T](mainStageProducer, viewSceneFactoryProducer)
