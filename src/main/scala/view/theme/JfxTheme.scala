package view.theme

import scalafx.scene.paint.Color


object JfxTheme extends Theme[Color]:

  override def primary: Color = Color.valueOf("#FFC136")

  override def secondary: Color = Color.rgb(96, 96, 96)

  override def tertiary: Color = Color.valueOf("#367BFF")

  override def primaryContainer: Color = Color.valueOf("#FFCB5C")

  override def secondaryContainer: Color = Color.rgb(128, 128, 128)

  override def tertiaryContainer: Color = Color.valueOf("#5C95FF")

  override def onPrimaryContainer: Color = Color.valueOf("#1A1100")

  override def onSecondaryContainer: Color = Color.rgb(64, 64, 64)

  override def onTertiaryContainer: Color = Color.valueOf("#001947")

  override def primaryBorder: Color = Color.valueOf("#FF5B36")

  override def secondaryBorder: Color = ???

  override def tertiaryBorder: Color = Color.valueOf("#0049D1")

  override def error: Color = ???

  override def errorContainer: Color = ???

  override def onErrorContainer: Color = ???

  override def errorBorder: Color = ???
