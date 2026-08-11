package controller

import controller.ViewPublisher.ViewContext
import controller.ViewPublisher.ViewContext.*
import model.ModelPublisher
import model.ModelPublisher.{ModelContext, ModelSubscriber}

class ModelPublisherBridge extends ModelSubscriber:
  this.setPublisher(ModelPublisher())

  override def update(context: ModelContext): Unit = ViewPublisher().notify(translateContext(context))

  private def translateContext(context: ModelContext): ViewContext = context match
    case ModelContext.ResourceContext => ResourceContext
    case ModelContext.MissionContext => MissionBoughtContext
    case ModelContext.TurnEndContext => TurnChangeContext
    case ModelContext.TurnStepContext => TurnStepChangeContext
    case ModelContext.PlayerMovedContext => PlayerMovedContext
    case ModelContext.EffectChoiceContext => PlayerChoiceContext
    case ModelContext.FaceObtainedContext => ItemObtainedContext
    case ModelContext.DieChoiceContext => SelectDieForThrowContext
    case ModelContext.DiceThrownContext => DiceThrownContext