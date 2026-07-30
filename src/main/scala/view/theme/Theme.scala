package view.theme

trait Theme[T]:

  /**
   * @return primary element colour
   */
  def primary: T

  /**
   * @return secondary element colour
   */
  def secondary: T

  /**
   * @return tertiary element colour
   */
  def tertiary: T

  /**
   * @return primary container colour
   */
  def primaryContainer: T

  /**
   * @return secondary container colour
   */
  def secondaryContainer: T

  /**
   * @return tertiary container colour
   */
  def tertiaryContainer: T

  /**
   * @return primary container text colour
   */
  def onPrimaryContainer: T

  /**
   * @return secondary container element colour
   */
  def onSecondaryContainer: T

  /**
   * @return tertiary container text colour
   */
  def onTertiaryContainer: T
