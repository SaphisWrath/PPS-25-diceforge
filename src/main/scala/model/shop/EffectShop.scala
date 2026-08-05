package model.shop

import model.effects.Effect
import model.resource.Resource

class EffectShop(items: (Effect, Resource)*) extends Shop[Effect]:
  private var _catalog: Set[(Effect, Resource)] = Set.from(items)
  private var _items = items.map((item, _) =>  item)

  override def getPrice(item: Effect): Option[Resource] =
    if _items.contains(item) then
      Some(_catalog.filter((e, _) => e == item).head._2)
    else
      None
