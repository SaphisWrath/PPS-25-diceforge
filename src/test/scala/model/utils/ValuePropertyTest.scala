package model.utils

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class ValuePropertyTest extends AnyFlatSpec with should.Matchers:

  private val initialValue = 0

  private def valueProperty: ValueProperty[Int] = ValueProperty(initialValue)

  "A ValueProperty" should "start with the given value" in :
    valueProperty.value should be(initialValue)

  it should "be able to update the value" in :
    val property = valueProperty
    val oldValue = property.value
    val newValue = oldValue + 1
    property.value = newValue

    property.value should be(newValue)

  it should "call the onChange function after a value change" in :
    val property = valueProperty
    val oldValue = valueProperty.value
    var oldValueBuffer = 0
    val newValue = oldValue + 1
    var newValueBuffer = 0
    var valueBuffer = 0
    property.onChange = (oldValue, newValue) =>
      oldValueBuffer = oldValue
      newValueBuffer = newValue
      valueBuffer = property.value
    property.value = newValue

    oldValueBuffer should be(oldValue)
    newValueBuffer should be(newValue)
    valueBuffer should be(newValue)

