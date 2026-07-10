package view.builders

import controller.GameController
import scalafx.beans.property.IntegerProperty
import scalafx.scene.Node
import scalafx.scene.control.Label
import scalafx.scene.layout.{HBox, Pane, VBox}
import view.ViewPublishers
import view.ViewPublishers.Context.{ResourceContext, ResourceMaxContext}
import view.ViewPublishers.{Subscriber, ViewPublisher}

object ResourceBoxes:

  trait ResourceBox:
    def component: Pane


  class BaseResourceBox(val resourceName: String, val amountProducer: ()=>Int ) extends ResourceBox with Subscriber:
    private val amount = IntegerProperty(amountProducer())
    private val amountLabel = Label(s"${amount()}")
    amount.onChange((_,_,_) => amountLabel.text = s"${amount()}")
    override def component: Pane = HBox(Label(s"$resourceName:"), amountLabel)

    override def update(context: ViewPublishers.Context): Unit = context match
      case ResourceContext => amount() = amountProducer()
      case _ =>

  class ResourceWithCapBox(val resourceName: String, val amountProducer: () => Int, val capProducer: () => Int) extends ResourceBox with Subscriber:
    private val baseResourceBox = BaseResourceBox(resourceName, amountProducer)
    private val cap = IntegerProperty(capProducer())
    private def labelContent: String = s"/${cap()}"
    private val capLabel = Label(labelContent)
    cap.onChange((_,_,_) => capLabel.text = labelContent)
    override def component: Pane =
      val box = baseResourceBox.component
      box.children ++= Seq(capLabel)
      box

    override def update(context: ViewPublishers.Context): Unit = context match
      case ResourceMaxContext => cap() = capProducer()
      case _ => baseResourceBox.update(context)
