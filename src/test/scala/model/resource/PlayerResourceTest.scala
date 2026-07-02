package model.resource

import model.resource.PlayerResource.unapply
import org.scalatest.funsuite.AnyFunSuite
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

class PlayerResourceSpec extends AnyWordSpec {

}
