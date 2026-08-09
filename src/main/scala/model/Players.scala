package model

import model.dice.Die
import model.dice.DieFactory.*
import model.missions.Obtained
import model.resource.PlayerBoard

object Players:
//TODO Make more expandible
  enum Color(string: String):
    case Orange extends Color("Orange")
    case Green extends Color("Green")
    case Black extends Color("Black")
    case Blue extends Color("Blue")
//TODO Complete Scala Doc
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
    
    def dice: Seq[Die]

    def missions: Seq[Obtained]

    def addMission(mission: Obtained): Unit

  object Player:
    private case class PlayerImpl(name: String, color: Color) extends Player:
      private var _missions: Seq[Obtained] = Seq.empty
      override val board: PlayerBoard = PlayerBoard.emptyBoard
      
      override val dice: Seq[Die] = Seq(mockOptionDie)

      override def missions: Seq[Obtained] = _missions

      override def addMission(mission: Obtained): Unit = _missions = _missions.appended(mission)

    def apply(name: String, color: Color): Player = PlayerImpl(name, color)

    def unapply(player: Player): (String, Color) = (player.name, player.color)