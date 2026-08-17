package view.builders

import controller.publishers.ViewPublisher
import controller.publishers.ViewPublisher.ViewContext.*
import controller.publishers.ViewPublisher.{ViewContext, ViewSubscriber}
import scalafx.beans.property.IntegerProperty
import scalafx.scene.control.Label
import scalafx.scene.layout.{HBox, Pane}

object ResourceBoxes:

  trait ResourceBox:
    def component: Pane

  class BaseResourceBox(val resourceName: String, val amountProducer: () => Int) extends ResourceBox with ViewSubscriber:
    this.subscribeTo(ViewPublisher())
    private val amount = IntegerProperty(amountProducer())
    private val amountLabel = Label(s"${amount()}")
    amount.onChange((_, _, _) => amountLabel.text = s"${amount()}")

    override def component: Pane = HBox(Label(s"$resourceName:"), amountLabel)

    override def update(context: ViewContext): Unit = context match
      case ResourceContext => amount() = amountProducer()
      case _ =>

  class ResourceWithCapBox(val resourceName: String, val amountProducer: () => Int, val capProducer: () => Int) extends ResourceBox with ViewSubscriber:
    this.subscribeTo(ViewPublisher())
    private val baseResourceBox = BaseResourceBox(resourceName, amountProducer)
    private val cap = IntegerProperty(capProducer())

    private def labelContent: String = s"/${cap()}"

    private val capLabel = Label(labelContent)
    cap.onChange((_, _, _) => capLabel.text = labelContent)

    override def component: Pane =
      val box = baseResourceBox.component
      box.children ++= Seq(capLabel)
      box

    override def update(context: ViewContext): Unit =
      if context == ResourceContext then cap() = capProducer()
      baseResourceBox.update(context)
