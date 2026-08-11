package view

import scalafx.scene.Node
import scalafx.scene.layout.StackPane

/**Used to create a redrawable element in scalafx
 *
 */
trait Redrawable:
  /**The concrete component to use in the view
   *
   * @return A [[Node]] instance equivalent to the content of the Redrawable
   */
  def component: Node

  /**Notify the object to redraw itself
   *
   */
  def redraw(): Unit

  /**An equivalent method to [[component]]
   * @return the same result as [[component]]
   */
  private def apply(): Node = component

object Redrawable:
  private class RedrawableImpl[E <: Node](private val componentProducer: () => E) extends Redrawable:
    private lazy val container: StackPane = new StackPane {
      children = componentProducer()
    }

    override def component: Node = container

    override def redraw(): Unit = container.children = componentProducer()

  def apply[E <: Node](componentProducer: () => E): Redrawable = RedrawableImpl(componentProducer)
