package view.builders

import controller.ViewPublishers.ViewPublisher
import scalafx.geometry.Insets
import scalafx.scene.control.Label
import scalafx.scene.layout.*
import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle
import scalafx.scene.{Group, Node}
import view.builders.ResourceBoxes.{BaseResourceBox, ResourceWithCapBox}

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

    def withResourceSection(
                             resourceProducers: Map[String, () => Int],
                             resourceCapProducers: Map[String, () => Int]
                           ): PlayerBoxBuilder =
      this.copy(
        resourceSection = new VBox {
          children = resourceProducers.map(pair =>
            val resource = pair._1
            val amountProducer = pair._2
            val resourceBox = if resourceCapProducers.exists(_._1 == resource) then
              ResourceWithCapBox(resource, amountProducer, resourceCapProducers(resource))
            else
              BaseResourceBox(resource, amountProducer)
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
      right = diceSection
    }
