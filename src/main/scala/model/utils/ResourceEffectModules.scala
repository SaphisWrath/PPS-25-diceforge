package model.utils

import model.resource.{PlayerBoard, Resource}

trait ResourceEffectModule:
  /**
   * Performs the requested resource operation on a board
   * @param board the board on which to perform the operation
   * @param resource the resource required for the operation
   */
  def apply(board: PlayerBoard, resource: Resource): Unit

object ResourceEffectModules:
  given AddResource: ResourceEffectModule:
    /**
     * Adds the resource
     */
    override def apply(board: PlayerBoard, resource: Resource): Unit =
      board.addResource(resource)

  given SubtractResource: ResourceEffectModule:
    /**
     * Subtracts the resource
     */
    override def apply(board: PlayerBoard, resource: Resource): Unit =
      board.takeResource(resource)