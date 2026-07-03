package view.builders

import scalafx.geometry.Insets
import scalafx.scene.{Group, Node}
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
object PlayerBoxes:
  abstract class PlayerBox:
    private[PlayerBoxes] def cornerRadii: CornerRadii = CornerRadii.Empty
    private[PlayerBoxes] def boxBorder: Border = Border.Empty
    private[PlayerBoxes] def boxBackground: Background = Background.Empty
    private[PlayerBoxes] def nameSection: Node = Group()
    private[PlayerBoxes] def tokenSection: Node = Group()
    private[PlayerBoxes] def resourceSection: Node = Group()
    def create: Node = new BorderPane {
      border = boxBorder
      background = boxBackground
      top = nameSection
      left = tokenSection
      center = resourceSection
    }

  case class BasePlayerBox(playerName: String) extends PlayerBox:
    override def nameSection: Node = Label(playerName)

  trait RoundedCorners(radius: Double) extends PlayerBox:
    override def cornerRadii: CornerRadii = CornerRadii(radius)

  trait FillBackground(color: Color) extends PlayerBox:
    override def boxBackground: Background =
      Background(Array(BackgroundFill(color, super.cornerRadii, Insets.Empty)))

  trait SolidBorder(color: Color, width: Int) extends PlayerBox:
    override def boxBorder: Border = Border(BorderStroke(color, BorderStrokeStyle.Solid, super.cornerRadii, BorderWidths(width)))

  trait CircleTokenSection(radius: Double, color: Color) extends PlayerBox:
    override def tokenSection: Node = Circle(radius, color)

  trait StandardResourceSection(resources: Seq[String]) extends PlayerBox: //TODO
    override def resourceSection: Node = new VBox {
      children = resources.map(Label(_))
    }
