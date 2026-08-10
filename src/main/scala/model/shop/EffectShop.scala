package model.shop

import model.ModelPublisher.ModelContext.{FaceObtainedContext, ResourceContext}
import model.{ModelPublisher, Players}
import model.effects.Effect
import model.resource.Resource

class EffectShop(initialItems: (Effect, Resource, Int)*) extends Shop[Effect]:
  private var _catalog: Map[Effect, (Resource, Int)] = Set.from(initialItems).map((e, r, a) => (e, (r, a))).toMap

  private def isInCatalog(item: Effect) = _catalog.contains(item)

  override def getPrice(item: Effect): Option[Resource] =
     if isInCatalog(item) then Some(_catalog(item)._1) else None

  override def getStocked(item: Effect): Option[Int] =
    if isInCatalog(item) then Some(_catalog(item)._2) else None

  override def buy(item: Effect, player: Players.Player): Boolean =
    if _catalog.contains(item) then
      val price = getPrice(item)
      if player.board.canSpend(price.get) then
        player.board.takeResource(price.get)
        _catalog.map((k, v) => if k == item then (k, (v._1, v._2 - 1)))
        _catalog = _catalog.updated(item, (getPrice(item).get, getStocked(item).get - 1))
        player.dice.foreach(_.setQueueFace(item))
        ModelPublisher().notify(ResourceContext)
        ModelPublisher().notify(FaceObtainedContext)
        true
      else false
    else false

  override def items: Seq[Effect] = _catalog.keys.toSeq
