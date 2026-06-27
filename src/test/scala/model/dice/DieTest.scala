package model.dice

import model.dice.Face.*
import model.dice.*
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

  test("Die roll can return every face it has") {
    import model.utils.RandomModules.given
    val coin = BaseDie(2)
    coin.addFaces(mock[SumFace])
    coin.addFaces(mock[OptionFace])

    var heads = false
    var tails = false

    while (!(heads && tails))
      val roll = coin.roll
      heads = heads || roll.isInstanceOf[SumFace]
      tails = tails || roll.isInstanceOf[OptionFace]

    assert(heads && tails)
  }
}
