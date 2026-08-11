package general_utils

object Publishers:

  /** Marker trait representing the context passed during a notification
   *
   * Defines the events emitted by a [[Publisher]] and consumed by a [[Subscriber]]
   */
  trait Context

  /** Defines a consumer in the Observer pattern capable of reacting to a context update
   * A [[Subscriber]] registers to one or more [[Publisher]] of a compatible type `C`
   * to receive notifications
   *
   * @tparam C The type of [[Context]] this subscriber receives
   */
  trait Subscriber[C <: Context]:
    /**
     * @param context
     * Updates itself based on his context
     */
    def update(context: C): Unit

    /**
     * @param publisher
     * Subscribe to the given publisher
     */
    def subscribeTo(publisher: Publisher[C]): Unit = publisher.subscribe(this)

  /** Defines a event source in the Observer pattern, responsible for managing subscriber registrations
   *
   * A [[Publisher]] maintains a collection of [[Subscriber]] instances and broadcast
   * events with a [[Context]] object.
   *
   *
   * @tparam C The type of [[Context]] this publisher emits
   */
  trait Publisher[C <: Context]:
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

    /**
     *
     * @param subscriber the subscriber to unsubscribe for this publisher
     */
    def unsubscribe(subscriber: Subscriber[C]): Unit

    /**
     * Unsubscribe all subscribers
     *
     */
    def reset(): Unit

  object Publisher:
    private class PublisherImpl[C <: Context] extends Publisher[C]:
      private var subscribers: Seq[Subscriber[C]] = Seq.empty

      def subscribe(subscriber: Subscriber[C]): Unit = subscribers = subscribers.appended(subscriber)

      def notify(context: C): Unit = subscribers.foreach(_.update(context))

      override def unsubscribe(subscriber: Subscriber[C]): Unit = subscribers = subscribers.diff(Seq(subscriber))

      override def reset(): Unit = subscribers = Seq.empty

    def apply[C <: Context](): Publisher[C] = PublisherImpl[C]()