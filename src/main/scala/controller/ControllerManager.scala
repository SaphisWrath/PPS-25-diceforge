package controller

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
    override val stageController: ControllerStage =
      ControllerStage(Navigator(mainStageProducer(), viewSceneFactoryProducer(this)))
    override val matchInitController: ControllerMatchInit = ControllerMatchInit()
    override val gameController: GameController = GameController(Seq(Player("paopl", Black)))
    override def matchEndController: ControllerMatchEnd = ControllerMatchEnd(???)//TODO Set as val
    
  def apply[T](
             mainStageProducer: () => MainStage[T],
             viewSceneFactoryProducer: ControllerManager => ViewSceneFactory[T]
           ): ControllerManager = ControllerManagerImpl[T](mainStageProducer, viewSceneFactoryProducer)
