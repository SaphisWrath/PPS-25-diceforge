package model.utils

import java.util.Date
import scala.util.Random

trait RandomModule[T]:
  def randomIndex(upperBound: T): T
  
object RandomModules:
  given RandomModule[Int] with
    override def randomIndex(upperBound: Int): Int =
      val random = Random(Date().getTime)
      random.between(1, upperBound)