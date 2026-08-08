package model.shop

import model.ModelPublisher.ModelContext.EffectBoughtContext
import model.{ModelPublisher, Players}
import model.effects.Effect
import model.resource.Resource

class EffectShop(initialItems: (Effect, Resource)*) extends Shop[Effect]:
  private val _catalog: Set[(Effect, Resource)] = Set.from(initialItems)
  private var _items = initialItems.map((item, _) =>  item)
  private var _lastItemBought: Option[Effect] = None

  override def getPrice(item: Effect): Resource = {
    if _catalog.isEmpty then throw IllegalStateException("Price catalog is empty.")
    else _catalog.filter((e, _) => e == item).head._2
  }

  override def buy(item: Effect, player: Players.Player): Unit =
    if _items.contains(item) then
      val price = getPrice(item)
      if player.board.canSpend(price) then
        player.board.takeResource(price)
        _items = _items.diff(Seq(item))
        _lastItemBought = Some(item)
        ModelPublisher().notify(EffectBoughtContext)
      else throw IllegalStateException(s"Player ${player.name} bought shop item without the necessary funds")
    else throw IllegalStateException("Bought shop item that wasn't in stock.")

  override def items: Seq[Effect] = _items

  override def lastItemBought: Option[Effect] = _lastItemBought
