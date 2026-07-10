package view.builders

import scalafx.geometry.Insets
import scalafx.scene.{Group, Node}
import scalafx.scene.control.Label
import scalafx.scene.input.KeyCode.Insert
import scalafx.scene.layout.Priority.Always
import scalafx.scene.layout.{Background, BackgroundFill, Border, BorderPane, BorderStroke, BorderStrokeStyle, BorderWidths, CornerRadii, FlowPane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle
import view.ViewPublishers.ViewPublisher
import view.builders.ResourceBoxes.BaseResourceBox

import scala.language.postfixOps

object PlayerBoxes:
  case class PlayerBoxStyle(
                             padding: Double,
                              cornerRadius: Double,
                              borderStyle: BorderStrokeStyle,
                              borderColor: Color,
                              borderWidth: Double,
                              backgroundColor: Color
                      ):
    val fxPadding: Insets = Insets(padding)
    val fxCornerRadii: CornerRadii = CornerRadii(cornerRadius)
    val fxBorder: Border = Border(BorderStroke(borderColor, borderStyle, fxCornerRadii, BorderWidths(borderWidth)))
    val fxBackground: Background = Background(Array(BackgroundFill(backgroundColor, fxCornerRadii, Insets.Empty)))

  object PlayerBoxStyle:
    val Standard = PlayerBoxStyle(10, 10, BorderStrokeStyle.Solid, Color.Black, 3, Color.Transparent)
    val Small = PlayerBoxStyle(5, 6, BorderStrokeStyle.Dashed, Color.Black, 2, Color.Transparent)

  case class PlayerBoxBuilder(
                               private val boxStyle: PlayerBoxStyle,
                               private val nameSection: Node = Group(),
                               private val tokenSection: Node = Group(),
                               private val resourceSection: Node = Group(),
                               private val diceSection: Node = Group()
                             ):
    def withNameSection(playerName: String): PlayerBoxBuilder =
      this.copy(nameSection = Label(playerName))

    def withCircleTokenSection(color: Color, radius: Double): PlayerBoxBuilder =
      this.copy(tokenSection = Circle(radius, color))

    def withResourceSection(playerName: String, resources: Seq[(String, Int)]): PlayerBoxBuilder =
      this.copy(
        resourceSection = new VBox {
          children = resources.map(pair => 
            val resourceBox = BaseResourceBox(playerName, pair._1, pair._2)
            resourceBox.setPublisher(ViewPublisher())
            resourceBox.component
          )
        }
      )
      
    def withDiceSection(): PlayerBoxBuilder = this //TODO

    def build: Node = new BorderPane {
      border = boxStyle.fxBorder
      background = boxStyle.fxBackground
      padding = boxStyle.fxPadding
      top = nameSection
      center = resourceSection
      left = tokenSection
      bottom = diceSection
    }
