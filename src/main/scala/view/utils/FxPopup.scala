package view.utils

import scalafx.stage.{Popup, Stage, Window}
import view.scenes.RulesScene

object FxPopup:
  private var _owner: Option[Window] = None
  private var _popup: Option[Popup] = None
  
  def setOwner(window: Window): Unit = {
    _owner = Some(window)
    val popup =new Popup()
    popup.content.addAll(RulesScene(() => popup.hide()).scene)
    _popup = Some(popup)
  }

  def showPopUp(): Unit =
    if _popup.isDefined & _owner.isDefined then
      _popup.get.show(_owner.get)