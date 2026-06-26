package model.dice

import model.dice.effects.Effects.ResourceEffect
import org.scalatest.funsuite.AnyFunSuite

class EffectSuite extends AnyFunSuite {

  test("Resource effect returns correct amount of resource") {
    assert(new ResourceEffect(2).solve() == 2)
    assert(new ResourceEffect(4).solve() == 4)
    assert(new ResourceEffect(6).solve() == 6)
  }
}
