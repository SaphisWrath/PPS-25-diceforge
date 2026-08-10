package controller

import model.Players.Color.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.language.postfixOps

class ControllerMatchInitTest extends AnyFlatSpec with Matchers:
  private val controller = ControllerMatchInit()

  "The controller" should "only accept players with a unique Name" in :
    controller.reset()
    controller.updateMatchInfo("Mario", Orange.toString)
    controller.isLastPlayerValid should be(true)

    controller.updateMatchInfo("Mario", Green.toString)
    controller.isLastPlayerValid should be(false)

  "The controller" should "only accept players with a unique Color" in :
    controller.reset()
    controller.updateMatchInfo("Mario", Orange.toString)
    controller.isLastPlayerValid should be(true)

    controller.updateMatchInfo("Luigi", Orange.toString)
    controller.isLastPlayerValid should be(false)

  "The controller" should "know when there are enough players to start the game" in :
    controller.reset()
    controller.updateMatchInfo("Mario", Orange.toString)
    controller.enoughPlayers should be(false)
    controller.updateMatchInfo("Luigi", Green.toString)
    controller.enoughPlayers should be(true)
    controller.updateMatchInfo("Toad", Blue.toString)
    controller.enoughPlayers should be(true)