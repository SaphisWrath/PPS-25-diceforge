package view

object ViewPublishers:
  enum Context:
    case ResourceContext
    case ResourceMaxContext
  
  trait Subscriber:
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
    def subscribe(subscriber: Subscriber): Unit

    /**
     * notify current subscribe that there as been a ResourceAmount change
     */
    def notifyResourceChange(): Unit

    /**
     * notify current subscribe that there as been a ResourceCap change
     */
    def notifyResourceCapChange(): Unit
    
  object ViewPublisher extends ViewPublisher:
    private var subscribers: Seq[Subscriber] = Seq.empty
    
    def subscribe(subscriber: Subscriber): Unit = subscribers = subscribers.appended(subscriber)
    
    private def notify(context: Context): Unit = subscribers.foreach(_.update(context))
    
    def notifyResourceChange(): Unit = notify(Context.ResourceContext)
    def notifyResourceCapChange(): Unit = notify(Context.ResourceMaxContext)
    
    def apply(): ViewPublisher = this