package model.dice

import model.dice.*
import model.effects.Target.Self
import model.effects.{CopyEffect, Effect, MultiplyEffect, ResourceEffect}
import model.resource.Gold
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar.mock

class DieSuite extends AnyFunSuite {
  val face: Effect = ResourceEffect(Gold(10), Self)

  test("addFace") {
    val maxFaces = 10
    val die = Die(maxFaces)
    for (i <- Iterable.range(0, maxFaces)) die.addFace(face)
    assert(die.faces.length == maxFaces)
  }

  test("roll"){
    import model.utils.RandomModules.given
    val die = Die(1)
    die.addFace(face)
    assert(die.roll.isInstanceOf[Effect])
  }
}

class DieSpec extends AnyFlatSpec:
  val maxFaces = 6
  val phEffect = ResourceEffect(Gold(3), Self)

  "Any die" should "allow to swap a selected face with a new face only when it's already full" in:
    val die = Die(maxFaces)
    val oldFace = CopyEffect()
    for (i <- Iterable.range(0, maxFaces - 1)) die.addFace(phEffect)
    die.addFace(oldFace)
    assert(die.faces.contains(oldFace))
    val newFace = MultiplyEffect(3)
    die.addFace(newFace, Some(oldFace))
    assert(die.faces.contains(newFace))
    assert(!die.faces.contains(oldFace))
    assertThrows[IllegalStateException](die.addFace(phEffect))

