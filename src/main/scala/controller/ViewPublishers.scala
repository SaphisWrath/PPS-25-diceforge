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
     * Notify all subscribers
     *
     * @param context The context of the message
     */
    def notify(context: Context): Unit

  object ViewPublisher extends ViewPublisher:
    private var subscribers: Seq[ViewSubscriber] = Seq.empty

    def subscribe(subscriber: ViewSubscriber): Unit = subscribers = subscribers.appended(subscriber)

    def notify(context: Context): Unit = subscribers.foreach(_.update(context))

    def apply(): ViewPublisher = this