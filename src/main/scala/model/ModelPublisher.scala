package model

import _root_.utils.Publishers.*

object ModelPublisher:
  enum ModelContext extends Context:
    case ResourceContext
    case ActionContext
    case MissionContext
    case TurnEndContext
    case TurnStepContext
    
  trait ModelSubscriber extends Subscriber[ModelContext]
  
  private val publisher = Publisher[ModelContext]()

  def apply(): Publisher[ModelContext] = publisher
