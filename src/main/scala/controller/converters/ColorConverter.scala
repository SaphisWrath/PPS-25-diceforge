package controller.converters

import model.Players.Color

object ColorConverter:
  private val ColorGreenHex = "#98D98E"
  private val ColorBlackHex = "#000000"
  private val ColorOrangeHex = "#fd6600"
  private val ColorBlueHex = "#1f51ff"
  extension (color: Color)
    def toHex: String = color match
      case Color.Green => ColorGreenHex
      case Color.Black => ColorBlackHex
      case Color.Orange => ColorOrangeHex
      case Color.Blue => ColorBlueHex
      
  extension (colorHex: String)
    def toColor: Color = colorHex match
      case ColorGreenHex => Color.Green
      case ColorBlackHex => Color.Black
      case ColorOrangeHex => Color.Orange
      case ColorBlueHex => Color.Blue
      case _ => throw IllegalArgumentException(s"Hex `$colorHex` doesn't correspond to any accepted color")
