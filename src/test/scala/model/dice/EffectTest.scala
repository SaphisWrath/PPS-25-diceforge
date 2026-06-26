package model.dice

import model.dice.effects.Effects.*
import org.scalatest.funsuite.AnyFunSuite

class EffectSuite extends AnyFunSuite {

  test("Resource effect returns correct amount of resource") {
    assert(new ResourceEffect(2).solve() == 2)
    assert(new ResourceEffect(4).solve() == 4)
    assert(new ResourceEffect(6).solve() == 6)
  }
  
  test("Multiply effect returns correct multiplier") {
    assert(new MultiplierEffect(3).solve() == 2)
    assert(new MultiplierEffect(4).solve() == 3)
    assert(new MultiplierEffect(5).solve() == 4)
  }
  
  test("Copy effect returns itself") {
    assert(new CopyEffect().solve().isInstanceOf[CopyEffect])
  }
}
