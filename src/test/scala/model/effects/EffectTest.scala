package model.effects

import model.Players.Color.Orange
import model.Players.Player
import model.resource.{Gold, PlayerBoard}
import org.scalatest.flatspec.AnyFlatSpec

class EffectTest extends AnyFlatSpec:
  private def emptyTestBoard: PlayerBoard = PlayerBoard.emptyBoard(Player("Mario", Orange))
  
  "A ResourceEffect" should "increase or decrease the player's resources by the given amount" in:
    val playerBoard = emptyTestBoard
    val amount = 20
    val resourceEffect = ResourceEffect(Gold(amount), Option(playerBoard))
    resourceEffect.resolve()
    val expectedAdd1 = if playerBoard.gold.maxCapacity > amount then amount else playerBoard.gold.maxCapacity
    assert(playerBoard.gold.amount == expectedAdd1)
    val expectedAdd2 = if playerBoard.gold.maxCapacity > amount * 2 then amount * 2 else playerBoard.gold.maxCapacity
    resourceEffect.resolve()
    assert(playerBoard.gold.amount == expectedAdd2)
    resourceEffect.setModule(model.utils.ResourceEffectModules.SubtractResource)
    val expectedSub = expectedAdd2 - resourceEffect.resource.amount
    resourceEffect.resolve()
    assert(playerBoard.gold.amount == (if expectedSub >= 0 then expectedSub else 0))
    
  "A MultiplyEffect" should "increase or decrease the player's resources by the multiplied amount" in:
    val playerBoard = emptyTestBoard
    val amount = 2
    val resourceEffect = ResourceEffect(Gold(amount), Option(playerBoard))
    val multiplyEffect = MultiplyEffect(3)
    multiplyEffect.setReceiver(playerBoard)
    
    multiplyEffect.resource = resourceEffect.resource
    resourceEffect.resolve()
    multiplyEffect.resolve()
    assert(playerBoard.gold.amount == 6)

    resourceEffect.setModule(model.utils.ResourceEffectModules.SubtractResource)
    multiplyEffect.setModule(model.utils.ResourceEffectModules.SubtractResource)
    resourceEffect.resolve()
    multiplyEffect.resolve()
    assert(playerBoard.gold.amount == 0)