package controller

import controller.MockControllers.mockGameController
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ControllerMatchEndTest extends AnyFlatSpec with Matchers:
  "The controller" should "correctly sort the players from best performance to worst" in:
    val controllerEnd = ControllerMatchEnd(mockGameController)
    controllerEnd.getSortedPlayers.map(_._1.getName) should be(List("Luigi", "Toad", "Mario"))