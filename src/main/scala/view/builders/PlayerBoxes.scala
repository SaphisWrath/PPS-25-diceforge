package view.builders

import controller.dto.EffectDTO
import scalafx.Includes.hex2sfxColor
import utils.Publishers.Publisher
import scalafx.geometry.Insets
import scalafx.scene.control.Label
import scalafx.scene.layout.*
import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle
import scalafx.scene.{Group, Node}
import view.builders.ResourceBoxes.{BaseResourceBox, ResourceWithCapBox}
import view.panes.EffectPanes.EffectWrapperPane

import scala.language.postfixOps



object PlayerBoxes:
  case class PlayerBoxStyle(
                             padding: Double = 0,
                             cornerRadius: Double = 0,
                             borderStyle: BorderStrokeStyle = BorderStrokeStyle.None,
                             borderColor: Color = Color.Transparent,
                             borderWidth: Double = 0,
                             backgroundColor: Color = Color.Transparent
                           ):
    val fxPadding: Insets = Insets(padding)
    val fxCornerRadii: CornerRadii = CornerRadii(cornerRadius)
    val fxBorder: Border = Border(BorderStroke(borderColor, borderStyle, fxCornerRadii, BorderWidths(borderWidth)))
    val fxBackground: Background = Background(Array(BackgroundFill(backgroundColor, fxCornerRadii, Insets.Empty)))

  object PlayerBoxStyle:
    val Standard = PlayerBoxStyle(
      padding = 10,
      cornerRadius = 10,
      borderStyle = BorderStrokeStyle.Solid,
      borderColor = Color.Black,
      borderWidth = 3,
      backgroundColor = Color.Transparent)
    val Small = PlayerBoxStyle(
      padding = 5,
      cornerRadius = 6,
      borderStyle = BorderStrokeStyle.Dashed,
      borderColor = Color.Black,
      borderWidth = 2,
      backgroundColor = Color.Transparent)
    val None = PlayerBoxStyle()
  
  def circleTokenComponent(color: Color, radius: Double): Node = Circle(radius, color)

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
            resourceBox.setPublisher(Publisher())
            resourceBox.component
          )
        }
      )

    def withDiceSection(
                       dieRollsProducer: () => Seq[Option[EffectDTO]],
                       colorHex: String
                       ): PlayerBoxBuilder = this.copy(
      diceSection = new HBox {
        private val dice = dieRollsProducer()
        if !dice.contains(None) then
          spacing = 10
          children = EffectWrapperPane("", dice.map(p =>  p.get), Color.valueOf(colorHex))
      }
    )
    def build: Node = new BorderPane {
      border = boxStyle.fxBorder
      background = boxStyle.fxBackground
      padding = boxStyle.fxPadding
      top = nameSection
      center = resourceSection
      left = tokenSection
      right = diceSection
    }
