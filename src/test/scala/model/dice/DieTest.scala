package model.dice

import model.dice.*
import model.dice.Face.*
import model.effects.Target.Self
import model.effects.{Effect, ResourceEffect}
import model.resource.Gold
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar.mock

class DieTest extends AnyFunSuite {
  val face: Effect = ResourceEffect(Gold(10), Self)

  test("Die requires correct number of faces.") {
    val maxFaces = 10
    val die = Die(maxFaces)
    for (i <- Iterable.range(0, maxFaces)) die.addFaces(face)
    assert(die.faces.length == maxFaces)
  }

  test("Die roll returns an effect."){
    import model.utils.RandomModules.given
    val die = Die(1)
    die.addFaces(face)
    assert(die.roll.isInstanceOf[Effect])
  }
}
