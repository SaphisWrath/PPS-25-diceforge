package view

object ViewPublishers:
  enum Context:
    case ResourceContext
    case ResourceMaxContext
  
  trait Subscriber:
    def update(context: Context): Unit
    def setPublisher(publisher: ViewPublisher): Unit = publisher.subscribe(this)
    
  trait ViewPublisher:
    def subscribe(subscriber: Subscriber): Unit
    def notifyResourceChange(): Unit
    def notifyResourceCapChange(): Unit
    
  object ViewPublisher extends ViewPublisher:
    private var subscribers: Seq[Subscriber] = Seq.empty
    
    def subscribe(subscriber: Subscriber): Unit = subscribers = subscribers.appended(subscriber)
    
    private def notify(context: Context): Unit = subscribers.foreach(_.update(context))
    
    def notifyResourceChange(): Unit = notify(Context.ResourceContext)
    def notifyResourceCapChange(): Unit = notify(Context.ResourceMaxContext)
    
    def apply(): ViewPublisher = this