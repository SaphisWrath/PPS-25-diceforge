package controller

import javafx.event.EventHandler
import scalafx.beans.property.ObjectProperty
import javafx.event.ActionEvent
import scalafx.scene.Scene
import view.scenes.MainMenuScene

trait ControllerMainMenu:
  def scene: Scene
  def onStart: ActionEvent => Unit
  def onRules: ActionEvent => Unit

object ControllerMainMenu:
  private class ControllerMainMenuImpl(
                                        val onStart: ActionEvent => Unit,
                                        val onRules: ActionEvent => Unit)
  extends ControllerMainMenu:
    val scene = new MainMenuScene(this)
  
  def apply(
             onStart: ActionEvent => Unit,
             onRules: ActionEvent => Unit
           ): ControllerMainMenu = new ControllerMainMenuImpl(onStart, onRules)
