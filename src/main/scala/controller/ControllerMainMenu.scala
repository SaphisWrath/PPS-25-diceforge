package controller

import javafx.event.EventHandler
import scalafx.beans.property.ObjectProperty
import javafx.event.ActionEvent
import scalafx.scene.Scene
import view.scenes.MainMenuScene

object ControllerMainMenu:
  def scene(onStart: ActionEvent => Unit, onRules: ActionEvent => Unit) = MainMenuScene(onStart, onRules)
  
