package controller.dto

case class ItemDTO(item: EffectDTO, cost: EffectDTO, stock: Int, onClick: () => Unit, clickable: () => Boolean)