package controller

object ViewPublishers:
  enum Context:
    case ResourceContext
    case ResourceMaxContext
    case MissionBoughtContext
    case TurnChangeContext

  trait ViewSubscriber:
    /**
     * @param context
     * Updates itself based on his context
     */
    def update(context: Context): Unit

    /**
     * @param publisher
     * Subscribe to the given publisher
     */
    def setPublisher(publisher: ViewPublisher): Unit = publisher.subscribe(this)

  trait ViewPublisher:
    /**
     * @param subscriber
     * Add given subscriber to the current subscribers
     */
    def subscribe(subscriber: ViewSubscriber): Unit

    /**
     * Notifies every subscriber
     * 
     * @param context the context required for the notification
     */
    def notify(context: Context): Unit

    /**
     * notify current subscribe that there as been a ResourceAmount change
     */
    def notifyResourceChange(): Unit

    /**
     * notify current subscribe that there as been a ResourceCap change
     */
    def notifyResourceCapChange(): Unit

  object ViewPublisher extends ViewPublisher:
    private var subscribers: Seq[ViewSubscriber] = Seq.empty

    def subscribe(subscriber: ViewSubscriber): Unit = subscribers = subscribers.appended(subscriber)

    def notify(context: Context): Unit = subscribers.foreach(_.update(context))

    def notifyResourceChange(): Unit = notify(Context.ResourceContext)

    def notifyResourceCapChange(): Unit = notify(Context.ResourceMaxContext)

    def apply(): ViewPublisher = this