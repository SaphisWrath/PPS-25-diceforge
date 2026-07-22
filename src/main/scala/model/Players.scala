package model

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
    def getName: String

    /**
     * @return The assigned color
     */
    def getColor: Color

  object Player:
    private class PlayerImpl(name: String, color: Color) extends Player:

      override def getName: String = name

      override def getColor: Color = color

    def apply(name: String, color: Color): Player = new PlayerImpl(name, color)