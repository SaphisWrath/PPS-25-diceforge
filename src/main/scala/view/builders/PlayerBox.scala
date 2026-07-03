package view.builders

import scalafx.geometry.Insets
import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.input.KeyCode.Insert
import scalafx.scene.layout.Priority.Always
import scalafx.scene.layout.{Background, BackgroundFill, Border, BorderPane, BorderStroke, BorderStrokeStyle, BorderWidths, CornerRadii, FlowPane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle

import scala.language.postfixOps

//  trait PlayerBoxBuilder:
//  
//    /**
//     * reverts all the changes made to the built node
//     */
//    def reset(): Unit
//  
//    /**
//     * @param name
//     * Sets the PlayerName to the given name
//     */
//    def buildName(name: String): Unit
//  
//    /**
//     * @param color
//     * sets the token to one of the given color
//     */
//    def buildToken(color: Color): Unit
//  
//    /**
//     * @param name
//     * Adds a resource with the given name
//     */
//    def buildResource(name: String): Unit
//  
//    /**
//     * Adds a dice Tracker TODO
//     */
//    def buildDiceTracker(): Unit
//  
//    /**
//     * Also call the reset function
//     * @return the node that has been built
//     */
//    def node: Node
object PlayerBox:
  

