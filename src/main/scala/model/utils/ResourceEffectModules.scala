package model.utils

import model.resource.{PlayerBoard, Resource}

trait ResourceEffectModule:
  def apply(board: PlayerBoard, resource: Resource): Unit

object ResourceEffectModules:
  given AddResource: ResourceEffectModule:
    override def apply(board: PlayerBoard, resource: Resource): Unit =
      board.addResource(resource)

  given SubtractResource: ResourceEffectModule:
    override def apply(board: PlayerBoard, resource: Resource): Unit =
      board.takeResource(resource)