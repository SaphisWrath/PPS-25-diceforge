package view

import scalafx.scene.Node
import scalafx.scene.layout.StackPane

trait Redrawable:
  def component: Node
  def redraw(): Unit
  def apply(): Node = component

object Redrawable:
  private class RedrawableImpl[E<: Node](private val componentProducer: () => E) extends Redrawable:
    private lazy val container: StackPane = new StackPane{
      children = componentProducer()
    }
    override def component: Node = container

    override def redraw(): Unit = container.children = componentProducer()

  def apply[E<: Node](componentProducer: ()=>E): Redrawable = RedrawableImpl(componentProducer)
