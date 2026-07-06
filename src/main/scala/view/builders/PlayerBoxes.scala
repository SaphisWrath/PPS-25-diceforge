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

object PlayerBoxes:
  case class PlayerBoxStyle(
                        private val _padding: Double,
                        private val _cornerRadius: Double,
                        private val _borderStyle: BorderStrokeStyle,
                        private val _borderColor: Color,
                        private val _borderWidth: Double,
                        private val _backgroundColor: Color
                      ):
    def padding: Insets = Insets(_padding)
    private def cornerRadii: CornerRadii = CornerRadii(_cornerRadius)
    def border: Border = Border(BorderStroke(_borderColor, _borderStyle, cornerRadii, BorderWidths(_borderWidth)))
    def background: Background = Background(Array(BackgroundFill(_backgroundColor, cornerRadii, Insets.Empty)))

  object PlayerBoxStyle:
    val Standard = PlayerBoxStyle(10, 10, BorderStrokeStyle.Solid, Color.Black, 3, Color.Transparent)
    val Small = PlayerBoxStyle(5, 6, BorderStrokeStyle.Dashed, Color.Black, 2, Color.Transparent)
    
    
  abstract class PlayerBox(boxStyle: PlayerBoxStyle):
    private[PlayerBoxes] def nameSection: Node = Group()
    private[PlayerBoxes] def tokenSection: Node = Group()
    private[PlayerBoxes] def resourceSection: Node = Group()
    private[PlayerBoxes] def diceSection: Node = Group()
    def create: Node = new BorderPane {
      padding = boxStyle.padding
      border = boxStyle.border
      background = boxStyle.background
      top = nameSection
      left = tokenSection
      center = resourceSection
      bottom = diceSection
    }

  case class BasePlayerBox(boxStyle: PlayerBoxStyle) extends PlayerBox(boxStyle)

  trait StandardNameSection(playerName: String) extends PlayerBox:
    override def nameSection: Node = Label(playerName)

  trait CircleTokenSection(radius: Double, color: Color) extends PlayerBox:
    override def tokenSection: Node = Circle(radius, color)

  trait StandardResourceSection(resources: Seq[String]) extends PlayerBox: //TODO
    override def resourceSection: Node = new VBox {
      children = resources.map(Label(_))
    }
