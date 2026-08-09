package model.shop

import model.Players.Player
import model.resource.Resource

trait Shop[T]:

  /**
   * Get the price of the requested item if it is in stock
   * @param item the requested item
   * @return the price of the item
   */
  def getPrice(item: T): Resource

  /**
   * Buy the selected item
   * @param item the item the player bought
   * @param player the player who wants to buy it
   */
  def buy(item: T, player: Player): Unit

  /**
   * @return the full list of items in stock
   */
  def items: Seq[T]