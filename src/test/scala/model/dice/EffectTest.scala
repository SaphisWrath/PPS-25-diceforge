package model.dice

import model.Players.Color.Orange
import model.Players.Player
import model.resource.*
import model.resource.PlayerBoard.unapply
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.language.postfixOps

class EffectTest extends AnyFlatSpec with Matchers:
  val player = Player("Mario", Orange)
  
  "A default resource effect" should "increase a player's resources" in:
    val playerResources = PlayerBoard(player,4,2,2,8)
    val resourceEffect = ResourceEffect(List(Gold(2), SunCrystal(1), MoonCrystal(1)))

    resourceEffect.effect.foreach(playerResources.addResource)
    unapply(playerResources) should be(unapply(PlayerBoard(player, 6, 3, 3, 8)))

  "A resource effect" can "decrease a player's resources" in:
    val playerResources = PlayerBoard(player,9,2,4,0)
    val resourceEffect = ResourceEffect(List(Gold(4)))

    resourceEffect.effect.foreach(playerResources.takeResource)
    unapply(playerResources) should be(unapply(PlayerBoard(player, 5, 2, 4, 0)))

  "A multiply effect" should "enhance a normal resource effect accordingly" in:
    val playerResources = PlayerBoard(player,4,2,2,8)
    val multiplierEffect = MultiplierEffect(3)

    multiplierEffect.setCurrentEffect(ResourceEffect(List(Gold(2), SunCrystal(1), MoonCrystal(1))))
    multiplierEffect.effect.foreach(playerResources.addResource)
    unapply(playerResources) should be(unapply(PlayerBoard(player, 10, 5, 5, 8)))

  "Different types of effect" can "be distinguished through match case functions" in:
    import org.scalatestplus.mockito.MockitoSugar.mock
    import model.dice.Face.SumFace

    val differentEffects: List[Effect[?]] = List(ResourceEffect(List(Gold(4))), GrantFaceEffect(mock[SumFace]))
    var firstIsResourceEffect = false

    differentEffects.head match
      case ResourceEffect(_) => firstIsResourceEffect = true

    firstIsResourceEffect should be(true)