package model.dice

import model.effects.Target.Self
import model.effects.*
import model.resource.{Gold, PlayerBoard, SunCrystal}
import model.utils.{RandomModule, TemporaryDie}

object MockDieFactory:
  private class MockDie(numFaces: Int) extends TemporaryDie:
    override def maxFaces: Int = numFaces
    private var faces: List[ResourceEffect] = List.empty
    private def isFull: Boolean = numFaces == faces.length

    private def addFace(face: ResourceEffect): Unit =
      if !isFull then faces = face :: faces

    override def addFaces(addedFaces: ResourceEffect*): Unit =
      addedFaces.foreach(f => this.addFace(f))

    override def roll(using randomModule: RandomModule[Int]): ResourceEffect =
      faces(randomModule.randomIndex(maxFaces))

  private def oneFaceDie(effect: ResourceEffect): TemporaryDie =
    val die = MockDie(1)
    die.addFaces(effect)
    die

  def mockGoldDie: TemporaryDie =
    oneFaceDie(ResourceEffect(Gold(2), Self))

  def mockCopyDie: TemporaryDie =
    oneFaceDie(CopyEffect())

  def mockOptionDie: TemporaryDie =
    oneFaceDie(OptionEffect(List(Gold(3), SunCrystal(2))))

  def mockMultiplyDie: TemporaryDie =
    oneFaceDie(MultiplyEffect(3))