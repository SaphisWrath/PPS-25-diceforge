package controller

import view.LanguageStrings

object ControllerProvider:
  def controllerMatchInit: ControllerMatchInit = ControllerMatchInitImpl()
  def controllerMatchEnd: ControllerMatchEnd = 
    ControllerMatchEndImpl(???)//TODO complete after modifying end game