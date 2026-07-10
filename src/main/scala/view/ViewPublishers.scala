package view

object ViewPublishers:
  enum Context:
    case ResourceContext
  
  trait Subscriber:
    def update(context: Context): Unit
    
  trait ViewPublisher:
    def subscribe(subscriber: Subscriber): Unit
    def notifyResourceChange(): Unit
    
  object ViewPublisher extends ViewPublisher:
    private var subscribers: Seq[Subscriber] = Seq.empty
    
    def subscribe(subscriber: Subscriber): Unit = subscribers = subscribers.appended(subscriber)
    
    private def notify(context: Context): Unit = subscribers.foreach(_.update(context))
    
    def notifyResourceChange(): Unit = notify(Context.ResourceContext)
    
    def apply(): ViewPublisher = this