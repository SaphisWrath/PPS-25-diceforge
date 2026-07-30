package model.effects

import model.Players.Color.Orange
import model.Players.Player
import model.effects.Target.Self
import model.resource.{Gold, PlayerBoard}
import org.scalatest.flatspec.AnyFlatSpec

class EffectTest extends AnyFlatSpec:
  "A ResourceEffect" should "increase or decrease the player's resources by the given amount" in:
    val player = Player("Mario", Orange)
    val amount = 20
    val resourceEffect = ResourceEffect(Gold(amount), Self)
    resourceEffect.resolve(player)
    val expectedAdd1 = if player.board.gold.maxCapacity > amount then amount else player.board.gold.maxCapacity
    assert(player.board.gold.amount == expectedAdd1)
    val expectedAdd2 = if player.board.gold.maxCapacity > amount * 2 then amount * 2 else player.board.gold.maxCapacity
    resourceEffect.resolve(player)
    assert(player.board.gold.amount == expectedAdd2)
    resourceEffect.setModule(model.utils.ResourceEffectModules.SubtractResource)
    val expectedSub = expectedAdd2 - resourceEffect.resource.amount
    resourceEffect.resolve(player)
    assert(player.board.gold.amount == (if expectedSub >= 0 then expectedSub else 0))
