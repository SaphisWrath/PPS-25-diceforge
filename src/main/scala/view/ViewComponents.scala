package view

import controller.{ControllerManager, ViewState}

/**Contains the traits used to build the base of the UI
 *
 * The components allow to create a UI while being agnostic from the technology
 * used to create the concrete graphic.
 */
object ViewComponents:

  /**The window of the UI
   *
   * @tparam T The unit of construction(es: Node)
   */
  trait MainStage[T]:
    /** Set the content of the stage
     *
     * @param scene The [[ViewScene]] to set as content
     */
    def setContent(scene: ViewScene[T]): Unit


  /**A scene of the UI
   *
   * @tparam T The unit of construction(es: Node)
   */
  trait ViewScene[T]:
    /**Return the concrete scene
     *
     * @return The instance of [[T]] that represent the content of the scene
     */
    def scene: T

    /**Alternative call of [[scene]]
     *
     * @return The same result of [[scene]]
     */
    final def apply(): T = this.scene

  /**Factory useful to create the scenes
   *
   * @tparam T The unit of construction(es: Node)
   * @tparam VS The subtype of [[ViewState]] used to identify the scenes
   * @param controllerManager The manager used to inject the dependencies of the views
   */
  trait ViewSceneFactory[T, VS <: ViewState](controllerManager: ControllerManager):
    /**Creates a ViewScene
     *
     * @param viewState The [[ViewState]] corresponding to the [[ViewScene]]
     * @return The instance of [[ViewScene]]
     */
    def createScene(viewState: VS): ViewScene[T]