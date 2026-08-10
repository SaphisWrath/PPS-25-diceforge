package model

import model.dice.{Die, DieFactory}
import model.missions.Obtained
import model.resource.PlayerBoard

object Players:
  enum Color(string: String):
    case Orange extends Color("Orange")
    case Green extends Color("Green")
    case Black extends Color("Black")
    case Blue extends Color("Blue")

  trait Player:

    /** The nickname of the player
     *
     *
     * @return The name of the player
     */
    def name: String

    /** The color representing the player
     *
     *
     * @return The assigned color
     */
    def color: Color

    /** The resource board of the player
     *
     * This board contains all the resources of the player
     *
     *
     * @return The [[PlayerBoard]] instance containing the resources of the player
     */
    def board: PlayerBoard

    /** The Dices of the player
     *
     *
     * @return A sequence containing the [[Dice]] instances
     */
    def dice: Seq[Die]

    /** The missions that the player obtained
     *
     *
     * @return A sequence containing all the [[Obtained]] instances
     */
    def missions: Seq[Obtained]

    /** Add an obtained mission to the mission list
     *
     *
     * @param mission The [[Obtained]] instance that should be added
     */
    def addMission(mission: Obtained): Unit

    /** How many rolls the player has to perform
     *
     *
     * @return The number of rolls the player has to perform
     */
    def pendingRolls: Int

    /** Set the number of pending rolls
     *
     *
     * @param rollsLeft The number of pending rolls to set
     */
    def pendingRolls_=(rollsLeft: Int): Unit

  object Player:
    private case class PlayerImpl(name: String, color: Color) extends Player:
      private var _missions: Seq[Obtained] = Seq.empty
      var pendingRolls = 0
      override val board: PlayerBoard = PlayerBoard.emptyBoard

      override val dice: Seq[Die] = DieFactory.makeStarterDice

      override def missions: Seq[Obtained] = _missions

      override def addMission(mission: Obtained): Unit = _missions = _missions.appended(mission)

    def apply(name: String, color: Color): Player = PlayerImpl(name, color)

    def unapply(player: Player): (String, Color) = (player.name, player.color)