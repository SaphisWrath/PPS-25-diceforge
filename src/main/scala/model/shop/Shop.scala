package model.shop

import model.resource.Resource

trait Shop[T]:

  /**
   * Get the price of the requested item if it is in stock
   * @param item the requested item
   * @return the price of the item
   */
  def getPrice(item: T): Option[Resource]