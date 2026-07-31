package model.dice

import model.dice.*
import model.dice.Face.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar.mock

class DieTest extends AnyFunSuite {
  val mockFace: Face = mock[SumFace]
  val die = BaseDie(6)
  for (i <- Iterable.range(0, 6)) die.addFaces(mockFace)

  test("Die requires correct number of faces.") {
    die.addFaces(mockFace)
    assert(die.faces.length == 6)
    assert(die.isFull)
  }

  test("Die roll returns a face.") {
    import model.utils.RandomModules.given
    assert(die.roll.isInstanceOf[Face])
    assert(die.roll.isInstanceOf[Face])
    assert(die.roll.isInstanceOf[Face])
    assert(die.roll.isInstanceOf[Face])
  }
}
