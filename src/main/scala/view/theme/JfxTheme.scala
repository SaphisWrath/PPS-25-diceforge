package view.theme

import scalafx.scene.paint.Color


object JfxTheme extends Theme[Color]:

  override def primary: Color = Color.valueOf("#FFC136")

  override def secondary: Color = Color.valueOf("#606060")

  override def tertiary: Color = Color.valueOf("#367BFF")

  override def primaryContainer: Color = Color.valueOf("#FFCB5C")

  override def secondaryContainer: Color = Color.valueOf("#808080")

  override def tertiaryContainer: Color = Color.valueOf("#5C95FF")

  override def onPrimaryContainer: Color = Color.valueOf("#1A1100")

  override def onSecondaryContainer: Color = Color.valueOf("#404040")

  override def onTertiaryContainer: Color = Color.valueOf("#001947")

  override def primaryBorder: Color = Color.valueOf("#FF5B36")

  override def secondaryBorder: Color = Color.valueOf("#242424")

  override def tertiaryBorder: Color = Color.valueOf("#0049D1")

  override def error: Color = Color.valueOf("#FF542E")

  override def errorContainer: Color = Color.valueOf("#FF7A5C")

  override def onErrorContainer: Color = Color.valueOf("#470D00")

  override def errorBorder: Color = Color.valueOf("#A31E00")
