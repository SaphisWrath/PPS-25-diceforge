package model

import model.Players.{Color, Player}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class PlayerTest extends AnyFlatSpec with should.Matchers:

  private val name = "name"
  private val color = Color.Green

  def player: Player = Player(name, color)

  "A Player" should "have a name" in :
    player.name should be(name)

  it should "have a color" in :
    player.color should be(color)