package view.builders

import controller.GameController
import scalafx.beans.property.IntegerProperty
import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.{HBox, VBox}
import view.ViewPublishers
import view.ViewPublishers.Context.ResourceContext
import view.ViewPublishers.{Subscriber, ViewPublisher}

object ResourceBoxes:

  trait ResourceBox:
    def component: Node


  class BaseResourceBox(val playerName: String, val resourceName: String, val amountProducer: ()=>Int ) extends ResourceBox with Subscriber:
    private val amount = IntegerProperty(amountProducer())
    private val amountLabel = Label(s"${amount.value}")
    amount.onChange((_,_,_) => amountLabel.text = s"${amount.value}")
    override def component: Node = HBox(Label(resourceName), amountLabel)

    override def update(context: ViewPublishers.Context): Unit = context match
      case ResourceContext => amount.value = amountProducer()
      case _ =>

