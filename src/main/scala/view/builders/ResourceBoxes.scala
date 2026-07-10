package view.builders

import controller.GameController
import scalafx.beans.property.IntegerProperty
import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.HBox
import view.ViewPublishers
import view.ViewPublishers.Context.ResourceContext
import view.ViewPublishers.{Subscriber, ViewPublisher}

object ResourceBoxes:

  trait ResourceBox:
    def setPublisher(publisher: ViewPublisher): Unit
    def component: Node


  class BaseResourceBox(val playerName: String, val resourceName: String, val initialAmount: Int) extends ResourceBox with Subscriber:
    private val amountProperty: IntegerProperty = IntegerProperty(initialAmount)

    private def createChildren(amount: Int): Seq[Node] = Seq(Label(s"$resourceName: $amount/Max"))//TODO insert maximum

    private val box: HBox = new HBox {
      children = createChildren(initialAmount)
    }

    amountProperty.onChange((_, _, newValue) => box.children = createChildren(newValue.intValue()))

    override def setPublisher(publisher: ViewPublisher): Unit = publisher.subscribe(this)

    override def update(context: ViewPublishers.Context): Unit = context match
      case ResourceContext => amountProperty.value = GameController.playerBoard(playerName).resourceMap(resourceName)
      case _ =>
    override def component: Node = box
