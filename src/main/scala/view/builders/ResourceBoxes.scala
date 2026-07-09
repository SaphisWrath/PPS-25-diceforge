package view.builders

import scalafx.beans.property.IntegerProperty
import scalafx.scene.Node
import view.ViewPublishers
import view.ViewPublishers.{Subscriber, ViewPublisher}

object ResourceBoxes:
  
  trait ResourceBox:
    def setPublisher(publisher: ViewPublisher): Unit
    def component: Node
    
    
  abstract class BaseResourceBox(val resourceName: String, private var amount: Int) extends ResourceBox with Subscriber:
    
    private val amountProperty: IntegerProperty = IntegerProperty(amount)
    
    override def setPublisher(publisher: ViewPublisher): Unit = publisher.subscribe(this)

    override def update(context: ViewPublishers.Context): Unit = ???
