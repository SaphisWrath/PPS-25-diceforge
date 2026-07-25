package controller

import view.LanguageStrings
import view.ViewComponents.{MainStage, ViewSceneFactory}

trait ControllerManager():
  def navigator: Navigator
  def gameController: GameController
  def controllerMatchInit: ControllerMatchInit
  def controllerMatchEnd: ControllerMatchEnd

object ControllerManager:
  private class ControllerManagerImpl[T](
                                          mainStageProducer: () => MainStage[T],
                                          viewSceneFactoryProducer: ControllerManager => ViewSceneFactory[T]
                                        ) extends ControllerManager:
    override val navigator: Navigator = Navigator(mainStageProducer(), viewSceneFactoryProducer(this))
    override def gameController: GameController = GameController
    override def controllerMatchInit: ControllerMatchInit = ControllerMatchInitImpl()
    override def controllerMatchEnd: ControllerMatchEnd = ControllerMatchEndImpl(???)
    
  def apply[T](
             mainStageProducer: () => MainStage[T],
             viewSceneFactoryProducer: ControllerManager => ViewSceneFactory[T]
           ): ControllerManager = ControllerManagerImpl[T](mainStageProducer, viewSceneFactoryProducer)
