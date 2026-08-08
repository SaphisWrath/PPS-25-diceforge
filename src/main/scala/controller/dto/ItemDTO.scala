package controller.dto

case class ItemDTO(item: EffectDTO, cost: EffectDTO, onClick: () => Unit, clickable: () => Boolean)