package utils

object Publishers:

  trait Context

  trait Subscriber[C<:Context]:
    /**
     * @param context
     * Updates itself based on his context
     */
    def update(context: C): Unit

    /**
     * @param publisher
     * Subscribe to the given publisher
     */
    def setPublisher(publisher: Publisher[C]): Unit = publisher.subscribe(this)

  trait Publisher[C<:Context]:
    /**
     * @param subscriber
     * Add given subscriber to the current subscribers
     */
    def subscribe(subscriber: Subscriber[C]): Unit

    /**
     * Notify all subscribers
     *
     * @param context The context of the message
     */
    def notify(context: C): Unit

  object Publisher:
    private class PublisherImpl[C<:Context] extends Publisher[C]:
      private var subscribers: Seq[Subscriber[C]] = Seq.empty

      def subscribe(subscriber: Subscriber[C]): Unit = subscribers = subscribers.appended(subscriber)

      def notify(context: C): Unit = subscribers.foreach(_.update(context))

    def apply[C<:Context](): Publisher[C] = PublisherImpl[C]()