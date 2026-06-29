package model

object Players:
  enum Color:
    case Orange
    case Green
    case Black
    case Blue

  object Color:
    val colorMap = Map(("Orange", Orange), ("Blue", Blue), ("Green", Green), ("Black", Black))
    def stringToColor(color: String): Option[Color] = colorMap.get(color)

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

      /**
       * @return The assigned color
       */
      override def getColor: Color = color

    def apply(name: String, color: Color): Player = new PlayerImpl(name, color)