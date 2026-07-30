package view.theme

import scalafx.scene.paint.Color


object JfxTheme extends Theme[Color]:

  override def primary: Color = Color.rgb(232, 175, 48)

  override def secondary: Color = Color.rgb(96, 96, 96)

  override def tertiary: Color = Color.rgb(51, 97, 172)

  override def primaryContainer: Color = Color.rgb(232, 199, 102)

  override def secondaryContainer: Color = Color.rgb(128, 128, 128)

  override def tertiaryContainer: Color = Color.rgb(116, 163, 245)

  override def onPrimaryContainer: Color = Color.rgb(50, 37, 9)

  override def onSecondaryContainer: Color = Color.rgb(64, 64, 64)

  override def onTertiaryContainer: Color = Color.rgb(20, 33, 57)
