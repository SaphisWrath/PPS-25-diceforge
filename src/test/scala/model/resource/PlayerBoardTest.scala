package model.resource

import model.Players.Color.Orange
import model.Players.Player
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class PlayerBoardTest extends AnyFlatSpec with Matchers:
  "When created, a player's board" should "have each resource set to 0" in:
    val playerResources = PlayerBoard.emptyBoard

    playerResources.gold.amount should be(0)
    playerResources.sunCrystals.amount should be(0)
    playerResources.moonCrystals.amount should be(0)
    playerResources.gloryPoints.amount should be(0)

  "Any PlayerBoard" should "refuse a transaction if the resources aren't sufficient" in:
    val board = PlayerBoard.emptyBoard
    assert(!board.canSpend(Gold(10)))
    assert(!board.canSpend(SunCrystal(10)))
    assert(!board.canSpend(MoonCrystal(10)))
    assert(!board.canSpend(GloryPoint(10)))
