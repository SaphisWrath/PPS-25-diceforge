package model.shop.factories

import model.shop.Shop

trait ShopFactory[T]:

  /**
   * @return the standard version of the required shop type
   */
  def makeStandardShop: Shop[T]