package controller

import model.GameMatch
import model.Players.Color.Black
import model.Players.Player
import view.LanguageStrings
import view.ViewComponents.{MainStage, ViewSceneFactory}

trait ControllerManager():
  def gameController: GameController
  def stageController: ControllerStage
  def matchInitController: ControllerMatchInit
  def matchEndController: ControllerMatchEnd

object ControllerManager:
  private class ControllerManagerImpl[T](
                                          mainStageProducer: () => MainStage[T],
                                          viewSceneFactoryProducer: ControllerManager => ViewSceneFactory[T]
                                        ) extends ControllerManager:
    private var gameMatch: Option[GameMatch] = Option.empty
    override val stageController: ControllerStage =
      ControllerStage(Navigator(mainStageProducer(), viewSceneFactoryProducer(this)))
    private val _matchInitController: ControllerMatchInit = ControllerMatchInit()
    override def matchInitController: ControllerMatchInit =
      _matchInitController.reset()
      _matchInitController
    override def gameController: GameController =
      gameMatch = Option(_matchInitController.builder.build())
      GameController(gameMatch.get)
    override def matchEndController: ControllerMatchEnd = ControllerMatchEnd(gameMatch.get)
    
  def apply[T](
             mainStageProducer: () => MainStage[T],
             viewSceneFactoryProducer: ControllerManager => ViewSceneFactory[T]
           ): ControllerManager = ControllerManagerImpl[T](mainStageProducer, viewSceneFactoryProducer)
