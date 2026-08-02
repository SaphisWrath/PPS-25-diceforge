package model.effects

import model.Players.Color.Orange
import model.Players.Player
import model.effects.Target.Self
import model.resource.{Gold, MoonCrystal, SunCrystal}
import org.scalatest.flatspec.AnyFlatSpec

class EffectTest extends AnyFlatSpec:
  private val player = Player("Mario", Orange)

  "A ResourceEffect" should "increase or decrease the player's resources by the given amount" in:
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
    resourceEffect.resolve(player)
    assert(player.board.gold.amount == (if expectedSub >= 0 then expectedSub else 0))

  "An OptionEffect" should "only update its current effect to one of its available options" in:
    val amount = 2
    val options = Seq(ResourceEffect(Gold(3), Self), ResourceEffect(SunCrystal(amount), Self))
    val optionEffect = OptionEffect(options)
    optionEffect.currentEffect = options(1)
    optionEffect.resolve(player)
    assert(player.board.sunCrystals.amount == amount)

    optionEffect.currentEffect = ResourceEffect(MoonCrystal(2), Self)
    optionEffect.resolve(player)
    assert(player.board.sunCrystals.amount == amount * 2)

  "An OptionEffect's default current effect" should "be its first option" in:
    val options = Seq(ResourceEffect(Gold(3), Self), ResourceEffect(SunCrystal(2), Self))
    val optionEffect = OptionEffect(options)
    assert(optionEffect.currentEffect == options.head)

  "A MultiplyEffect" should "increase or decrease the player's resources by the multiplied amount" in:
    val amount = 2
    val resourceEffect = ResourceEffect(Gold(amount), Self)
    val multiplyEffect = MultiplyEffect(3)

    multiplyEffect.currentEffect = resourceEffect
    resourceEffect.resolve(player)
    multiplyEffect.resolve(player)
    assert(player.board.gold.amount == 6)

    resourceEffect.setModule(model.utils.ResourceEffectModules.SubtractResource)
    multiplyEffect.currentEffect = resourceEffect
    resourceEffect.resolve(player)
    multiplyEffect.resolve(player)
    assert(player.board.gold.amount == 0)

  "A MultiplyEffect" should "not be used more than once without setting its current effect again" in:
    val amount = 2
    val resourceEffect = ResourceEffect(Gold(amount), Self)
    val multiplyEffect = MultiplyEffect(3)

    multiplyEffect.currentEffect = resourceEffect
    resourceEffect.resolve(player)
    multiplyEffect.resolve(player)
    assert(player.board.gold.amount == 6)

    multiplyEffect.resolve(player)
    assert(player.board.gold.amount == 6)