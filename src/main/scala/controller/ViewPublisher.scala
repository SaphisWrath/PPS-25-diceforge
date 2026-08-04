package controller

import utils.Publishers.{Context, Publisher, Subscriber}

object ViewPublisher:
  enum ViewContext extends Context:
    case ResourceContext
    case ResourceMaxContext
    case MissionBoughtContext
    case TurnChangeContext
    case ActionContext
    case ExtraActionContext

  trait ViewSubscriber extends Subscriber[ViewContext]

  private val publisher = Publisher[ViewContext]()

  def apply(): Publisher[ViewContext] = publisher