package view.builders

import scalafx.geometry.Insets
import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.input.KeyCode.Insert
import scalafx.scene.layout.Priority.Always
import scalafx.scene.layout.{Border, BorderPane, BorderStroke, BorderStrokeStyle, BorderWidths, CornerRadii, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle

import scala.language.postfixOps

trait PlayerBoxBuilder:

  /**
   * reverts all the changes made to the built node
   */
  def reset(): Unit

  /**
   * @param name
   * Sets the PlayerName to the given name
   */
  def buildName(name: String): Unit

  /**
   * @param color
   * sets the token to one of the given color
   */
  def buildToken(color: Color): Unit


  /**
   * @param name
   * Adds a resource with the given name
   */
  def buildResource(name: String): Unit

  /**
   * Adds a dice Tracker TODO
   */
  def buildDiceTracker(): Unit

  /**
   * Also call the reset function
   * @return the node that has been built
   */
  def node: Node

object PlayerBoxBuilder:
  private class PlayerBoxBuilderImpl extends PlayerBoxBuilder:
    private var _node: BorderPane = baseNode
    private var _resourceBox: VBox = VBox()

    override def reset(): Unit =
      _resourceBox = VBox()
      _node = baseNode

    override def buildName(name: String): Unit =
      _node.top = Label(name)

    override def buildToken(color: Color): Unit =
      _node.left = Circle(50, color)

    override def buildResource(name: String): Unit =
      _resourceBox.children += Label(name)

    override def buildDiceTracker(): Unit =
      _node.bottom = Label("DiceTracker")

    override def node: Node =
      val completeNode = _node
      reset()
      completeNode

    private def baseNode: BorderPane = new BorderPane {
      center = _resourceBox
      border = Border(BorderStroke(
        Color.Black,
        BorderStrokeStyle.Solid,
        CornerRadii(10),
        BorderWidths(3)
      ))
      padding = Insets(5, 10, 5, 10)
    }

  def apply(): PlayerBoxBuilder = PlayerBoxBuilderImpl()