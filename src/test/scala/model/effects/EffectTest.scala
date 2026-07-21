package model.effects

import model.resource.{Gold, PlayerBoard}
import org.scalatest.flatspec.AnyFlatSpec

class EffectTest extends AnyFlatSpec:
  "A ResourceEffect" should "increase or decrease the player's resources by the given amount" in:
    val playerBoard = PlayerBoard.emptyBoard
    val amount = 10
    val resourceEffect = ResourceEffect(Gold(amount), Option(playerBoard))
    import model.utils.ResourceEffectModules.AddResource
    resourceEffect.resolve()
    assert(playerBoard.gold.amount == amount)
    resourceEffect.resolve()
    assert(playerBoard.gold.amount == {
      if playerBoard.gold.maxCapacity > amount * 2 then amount * 2 else playerBoard.gold.maxCapacity
    })

