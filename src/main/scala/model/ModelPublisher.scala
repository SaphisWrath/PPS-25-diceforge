package model

import general_utils.Publishers.*

object ModelPublisher:
  enum ModelContext extends Context:
    case ResourceContext
    case MissionContext
    case TurnEndContext
    case TurnStepContext
    case PlayerMovedContext
    case EffectChoiceContext
    case DieChoiceContext
    case FaceObtainedContext
    case DiceThrownContext

  trait ModelSubscriber extends Subscriber[ModelContext]
  
  private val publisher = Publisher[ModelContext]()

  def apply(): Publisher[ModelContext] = publisher
