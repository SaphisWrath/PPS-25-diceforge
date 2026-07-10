package view.utils

import model.Players.Color.{Black, Blue, Green, Orange}

object ColorConversion:

  extension (color: model.Players.Color)
    def toScalaFX: scalafx.scene.paint.Color = color match
      case Green => scalafx.scene.paint.Color.Green
      case Black => scalafx.scene.paint.Color.Black
      case Orange => scalafx.scene.paint.Color.Orange
      case Blue => scalafx.scene.paint.Color.Blue
