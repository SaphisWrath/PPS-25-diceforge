package controller

import utils.Publishers.{Context, Publisher, Subscriber}

object ViewPublisher:
  enum ViewContext extends Context:
    case ResourceContext
    case ResourceMaxContext
    case MissionBoughtContext
    case TurnChangeContext
    case TurnStepChangeContext
    case PlayerMovedContext
    case PlayerChoiceContext
    case ItemBoughtContext

  trait ViewSubscriber extends Subscriber[ViewContext]

  private val publisher = Publisher[ViewContext]()

  def apply(): Publisher[ViewContext] = publisher