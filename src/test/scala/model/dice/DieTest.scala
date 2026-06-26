package model.dice

import model.dice.Face.*
import model.dice.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.mockito.MockitoSugar.mock

class DieTest extends AnyFunSuite {
  val mockFace: Face = mock[SumFace]

  test("Die requires correct number of faces.") {
    val die = BaseDie(6)
    for (i <- Iterable.range(0, 7)) die.addFaces(mockFace)
    assert(die.faces.length == 6)
    assert(die.isFull)
  }
}
