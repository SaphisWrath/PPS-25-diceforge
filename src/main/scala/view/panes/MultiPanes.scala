package view.panes

import scalafx.beans.property.ObjectProperty
import scalafx.scene.Node
import scalafx.scene.layout.{BorderPane, Pane, StackPane}
import view.scenes.ViewComponent


object MultiPanes:

  trait MultiPaneState:
    def id: String

  object MultiPaneState:
    private case class MultiPaneStateImpl(id: String) extends MultiPaneState
    def empty: MultiPaneState = MultiPaneStateImpl("")
    def apply(id: String): MultiPaneState =
      if id.isBlank then empty else MultiPaneStateImpl(id)

  class MultiPane(
                   private val nodeProducer: MultiPaneState => Node,
                   val acceptedStates: Set[MultiPaneState]
                 ) extends ViewComponent:
    private val container: StackPane = StackPane()
    private val state: ObjectProperty[MultiPaneState] = ObjectProperty(MultiPaneState.empty)
    state.onChange((_, _, newVal) => container.children = nodeProducer(newVal))

    def setState(multiPaneState: MultiPaneState): Unit =
      if acceptedStates.contains(multiPaneState) then
        state() = multiPaneState
      else
        throw IllegalArgumentException(s"${multiPaneState.id} doesn't correspond to any known state")
        
    def currentState: MultiPaneState = state()

    override def component: Node = container