package model

import model.missions.Mission
import model.resource.PlayerBoard

object Players:
  enum Color(string: String):
    case Orange extends Color("Orange")
    case Green extends Color("Green")
    case Black extends Color("Black")
    case Blue extends Color("Blue")

  trait Player:

    /**
     * @return The name of the player
     */
    def name: String

    /**
     * @return The assigned color
     */
    def color: Color

    def board: PlayerBoard

    def missions: Set[Mission]

  object Player:
    private case class PlayerImpl(name: String, color: Color) extends Player:
      override val board: PlayerBoard = PlayerBoard.emptyBoard(this)

      override def missions: Set[Mission] = Set.empty

    def apply(name: String, color: Color): Player = PlayerImpl(name, color)

    def unapply(player: Player): (String, Color) = (player.name, player.color)