package model.resource

import model.resource.PlayerResource.unapply
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PlayerResourceSuite extends AnyFunSuite {
  test("PlayerResource apply") {
    val player = PlayerResource(10, Gold(0))
    assert(player.resource == Gold(0))
    assert(player.maximumCapacity == 10)
  }

  test("PlayerResource unapply") {
    val player = PlayerResource(20, Gold(0))
    assert(unapply(player).contains((20, Gold(0))))
  }
}

class PlayerResourceSpec extends AnyFlatSpec with Matchers {
  "Modification through setMaximum" should "not dip below 1" in:
    val playerResource = PlayerResource(20, Gold(0))
    playerResource.setMaxAmount(0)
    assert(playerResource.maximumCapacity == 1)
    playerResource.setMaxAmount(-4)
    assert(playerResource.maximumCapacity == 1)

  "Modification through setResource" should "not allow resource to be over maximum" in:
    val maxAmount = 10
    val playerResource = PlayerResource(maxAmount, Gold(30))
    assert(playerResource.resource == Gold(0))
    playerResource.setResource(Gold(11))
    assert(playerResource.resource == Gold(maxAmount))

  "Modification through setResource" should "not allow resource to dip below 0" in:
    val playerResource = PlayerResource(10, Gold(-1))
    assert(playerResource.resource == Gold(0))
    playerResource.setResource(Gold(-5))
    assert(playerResource.resource == Gold(0))
}
