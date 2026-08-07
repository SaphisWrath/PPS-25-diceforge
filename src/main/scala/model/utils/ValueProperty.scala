package model.utils
//TODO: Understand if still useful
trait ValueProperty[T]:
  /**
   * A getter for the wrapped value
   *
   *
   * @return The wrapped value
   */
  def value: T

  /**
   * A setter for the wrapped value. It updates the value and then call the onChange function
   *
   *
   * @param newValue the new wrapped value
   */
  def value_=(newValue: T): Unit

  /**
   * Set the onChange function
   *
   *
   * @param onChangeFun The new onChange Function
   */
  def onChange(onChangeFun: (oldValue: T, newValue: T) => Unit): Unit

  /**
   * An alternative to onChange().
   *
   *
   * @param onChangeFun The new onChange Function
   */
  def onChange_=(onChangeFun: (oldValue: T, newValue: T) => Unit): Unit = this.onChange(onChangeFun)

object ValueProperty:
  private case class ValuePropertyImpl[T](
                                           private var _value: T,
                                           private var _onChange: (T, T) => Unit = (_: T, _: T) => {}
                                         ) extends ValueProperty[T]:
    override def value: T = _value

    override def value_=(newValue: T): Unit =
      val oldVal = _value
      _value = newValue
      _onChange(oldVal, newValue)

    override def onChange(onChangeFun: (T, T) => Unit): Unit = _onChange = onChangeFun

  def apply[T](value: T): ValueProperty[T] = ValuePropertyImpl(value)

  def apply[T](value: T, onChange: (T, T) => Unit): ValueProperty[T] = ValuePropertyImpl(value, onChange)