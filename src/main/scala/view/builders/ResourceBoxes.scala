package view.builders

import controller.ViewPublisher
import controller.ViewPublisher.{ViewContext, ViewSubscriber}
import controller.ViewPublisher.ViewContext.*
import utils.Publishers.Subscriber
import scalafx.beans.property.IntegerProperty
import scalafx.scene.control.Label
import scalafx.scene.layout.{HBox, Pane}
import utils.Publishers

object ResourceBoxes:

  trait ResourceBox:
    def component: Pane


  class BaseResourceBox(val resourceName: String, val amountProducer: () => Int) extends ResourceBox with ViewSubscriber:
    this.setPublisher(ViewPublisher())
    private val amount = IntegerProperty(amountProducer())
    private val amountLabel = Label(s"${amount()}")
    amount.onChange((_, _, _) => amountLabel.text = s"${amount()}")

    override def component: Pane = HBox(Label(s"$resourceName:"), amountLabel)

    override def update(context: ViewContext): Unit = context match
      case ResourceContext => amount() = amountProducer()
      case _ =>

  class ResourceWithCapBox(val resourceName: String, val amountProducer: () => Int, val capProducer: () => Int) extends ResourceBox with ViewSubscriber:
    private val baseResourceBox = BaseResourceBox(resourceName, amountProducer)
    private val cap = IntegerProperty(capProducer())

    private def labelContent: String = s"/${cap()}"

    private val capLabel = Label(labelContent)
    cap.onChange((_, _, _) => capLabel.text = labelContent)

    override def component: Pane =
      val box = baseResourceBox.component
      box.children ++= Seq(capLabel)
      box

    override def update(context: ViewContext): Unit = context match
      case ResourceMaxContext => cap() = capProducer()
      case _ => baseResourceBox.update(context)
