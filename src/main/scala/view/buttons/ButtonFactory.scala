package view.buttons

trait ButtonFactory[Button, Icon]:
  /**
   * @param text the button label
   * @param onClick the button onClick
   * @return A button styled for the main menu
   */
  def makeMenuButton(
                     text: String,
                     onClick: () => Unit
                   ): Button

  /**
   * @param buttonText the button lbel
   * @param onClick the button onClick
   * @param isDisabled a producer for the value of the disabled param of the button
   * @return A button styled for the Board
   */
  def makeBoardButton(buttonText: String, onClick: () => Unit, isDisabled: () => Boolean = () => false): Button

  /**
   * @param icon the icon for the choice button
   * @param onClick the onClick for the button
   * @return a button styled for the choice dialog
   */
  def makeChoiceButton(icon: Icon, onClick: () => Unit): Button