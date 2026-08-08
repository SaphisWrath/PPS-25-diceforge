package controller

import model.Players.Color.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.language.postfixOps

class ControllerMatchInitTest extends AnyFlatSpec with Matchers:
  private val controller = ControllerMatchInit(MatchBuilderImpl())

  "The controller" should "only accept players with a unique Name" in :
    controller.reset()
    controller.updateMatchInfo("Mario", Orange)
    controller.isLastPlayerValid should be(true)

    controller.updateMatchInfo("Mario", Green)
    controller.isLastPlayerValid should be(false)

  "The controller" should "only accept players with a unique Color" in :
    controller.reset()
    controller.updateMatchInfo("Mario", Orange)
    controller.isLastPlayerValid should be(true)

    controller.updateMatchInfo("Luigi", Orange)
    controller.isLastPlayerValid should be(false)

  "The controller" should "know when there are enough players to start the game" in :
    controller.reset()
    controller.updateMatchInfo("Mario", Orange)
    controller.enoughPlayers should be(false)
    controller.updateMatchInfo("Luigi", Green)
    controller.enoughPlayers should be(true)
    controller.updateMatchInfo("Toad", Blue)
    controller.enoughPlayers should be(true)