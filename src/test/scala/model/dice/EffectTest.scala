package model.dice

import model.resource.ResourceBoard
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.language.postfixOps

class EffectTest extends AnyFlatSpec with Matchers:
  "A default resource effect" should "increase a player's resources" in:
    val playerResources = ResourceBoard.board(4,2,2,8)
    val resourceEffect = ResourceEffect(ResourceBoard.board(2,1,1,0))

    playerResources + resourceEffect.effect should be(ResourceBoard.board(6,3,3,8))

  "A resource effect" can "decrease a player's resources" in:
    val playerResources = ResourceBoard.board(9,2,4,0)
    val resourceEffect = ResourceEffect(ResourceBoard.board(4,0,0,0))

    playerResources - resourceEffect.effect should be(ResourceBoard.board(5,2,4,0))

  "A multiply effect" should "enhance a normal resource effect accordingly" in:
    val playerResources = ResourceBoard.board(4,2,2,8)
    val multiplierEffect = MultiplierEffect(3)

    multiplierEffect.setCurrentEffect(ResourceEffect(ResourceBoard.board(2,1,1,0)))
    playerResources + multiplierEffect.effect should be(ResourceBoard.board(10,5,5,8))
