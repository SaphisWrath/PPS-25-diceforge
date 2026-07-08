package controller

import javafx.event.EventHandler
import scalafx.beans.property.ObjectProperty
import javafx.event.ActionEvent
import scalafx.scene.Scene
import view.scenes.MainMenuScene

trait ControllerMainMenu:
  def scene: Scene
  def onStart: ObjectProperty[EventHandler[ActionEvent]]
  def onRules: ObjectProperty[EventHandler[ActionEvent]]

object ControllerMainMenu:
  private class ControllerMainMenuImpl(
                                        val onStart: ObjectProperty[EventHandler[ActionEvent]],
                                        val onRules: ObjectProperty[EventHandler[ActionEvent]]) 
  extends ControllerMainMenu:
    val scene = new MainMenuScene(this)
  
  def apply(
             onStart: ObjectProperty[EventHandler[ActionEvent]],
             onRules: ObjectProperty[EventHandler[ActionEvent]]
           ): ControllerMainMenu = new ControllerMainMenuImpl(onStart, onRules)
