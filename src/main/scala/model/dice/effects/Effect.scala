package model.dice.effects

trait Effect[T]:
  def solve(): T
  
object Effects:
  class ResourceEffect(val amount: Int) extends Effect[Int]:
    override def solve(): Int = amount
    
  