package view.text

trait TextFactory[Text, Label]:
  /**
   * @return a Text element styled for the main menu title
   */
  def makeMenuTitle: Text

  /**
   * @param text the label text
   * @param parentWidth the width of the parent of the label
   * @param parentHeight the height of the parent of the label
   * @return a Label styled for the rules popup
   */
  def makeRulesLabel(text: String, parentWidth: Double, parentHeight: Double): Label

  /**
   * @param name the name of the mission
   * @return A Text element styled for the mission title
   */
  def makeMissionName(name: String): Text

  /**
   * @param label the text
   * @return a Text element styled for the mission label
   */
  def makeMissionLabel(label: String): Text

  /**
   * @param label the text
   * @return a Text element styled for the turn counter
   */
  def makeTurnCounterText(label: String): Text

  /**
   * @param label the text
   * @return A Text element styled for overlaying on an effect sprite
   */
  def makeEffectText(label: String): Text

  /**
   * @param label the text
   * @return A Text element styled for overlaying on a CompoundEffect sprite
   */
  def makeCompoundEffectText(label: String): Text
