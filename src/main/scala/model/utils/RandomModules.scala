package model.utils

import java.util.Date
import scala.util.Random

trait RandomModule[T]:
  /**
   * @param upperBound the upper bound for generating an index
   * @return a randomly generated index
   */
  def randomIndex(upperBound: T): T

object RandomModules:
  given RandomModule[Int] with
    /**
     * @return a randomly generated Int betweeen 0 and upperBound
     */
    override def randomIndex(upperBound: Int): Int =
      val random = new Random()
      random.nextInt(upperBound)