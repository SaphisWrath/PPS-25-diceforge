package model.utils

trait ValueProperty[T]:
  def value: T
  def value_=(newValue: T): Unit
  def onChange(fun: (oldValue: T, newValue: T) => Unit): Unit
  def onChange_=(fun: (oldValue: T, newValue: T) => Unit): Unit = this.onChange(fun)

object ValueProperty:
  private case class ValuePropertyImpl[T](
                                           private var _value: T,
                                           private var _onChange: (T, T) => Unit = (_: T, _:T) => {}
                                         ) extends ValueProperty[T]:
    override def value: T = _value

    override def value_=(newValue: T): Unit =
      val oldVal = _value
      _value = newValue
      _onChange(oldVal, newValue)

    override def onChange(fun: (T, T) => Unit): Unit = _onChange = fun

  def apply[T](value: T): ValueProperty[T] = ValuePropertyImpl(value)
  def apply[T](value: T, onChange: (T,T)=>Unit): ValueProperty[T] = ValuePropertyImpl(value, onChange)