package model.dice.effects

trait Effect[T]:
  def solve(): T
  
object Effects:
  class ResourceEffect(val amount: Int) extends Effect[Int]:
    override def solve(): Int = amount
    
  class MultiplierEffect(flatMultiplier: Int) extends Effect[Int]:
    val multiplier: Int = flatMultiplier - 1
    
    override def solve(): Int = multiplier
    
  class CopyEffect extends Effect[CopyEffect]:
    override def solve(): CopyEffect = this