package model.shop

import model.Players.Player
import model.resource.Resource

trait Shop[T]:

  /**
   * Get the price of the requested item if it is in stock
   * @param item the requested item
   * @return the price of the item
   */
  def getPrice(item: T): Option[Resource]

  /**
   * @param item the item whose availability we want to check
   * @return the number of available item copies
   */
  def getStocked(item: T): Option[Int]

  /**
   * Buy the selected item
   * @param item the item the player bought
   * @param player the player who wants to buy it
   * @return true if player bought item, false if not
   */
  def buy(item: T, player: Player): Boolean

  /**
   * @return the full list of items in stock
   */
  def items: Seq[T]