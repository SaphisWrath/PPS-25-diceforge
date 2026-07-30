package model

import model.missions.{Mission, ObtainedMission}
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

    def missions: Seq[Mission]

    def addMission(mission: Mission): Unit

  object Player:
    private case class PlayerImpl(name: String, color: Color) extends Player:
      private var _missions: Seq[Mission] = Seq.empty
      override val board: PlayerBoard = PlayerBoard.emptyBoard

      override def missions: Seq[Mission] = _missions

      override def addMission(mission: Mission): Unit = _missions = _missions.appended(mission)

    def apply(name: String, color: Color): Player = PlayerImpl(name, color)

    def unapply(player: Player): (String, Color) = (player.name, player.color)