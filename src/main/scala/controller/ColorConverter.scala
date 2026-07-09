package controller

import model.Players.Color

object ColorConverter:
  extension (color: Color)
    def toHex: String = color match
      case Color.Green => "#98D98E"
      case Color.Black => "#000000"
      case Color.Orange => "#fd6600"
      case Color.Blue => "#1f51ff"
