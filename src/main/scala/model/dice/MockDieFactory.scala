package model.dice

import model.effects.{CarriesResource, CopyEffect, MultiplyEffect, OptionEffect, ResourceEffect}
import model.resource.{Gold, PlayerBoard, SunCrystal}
import model.utils.{RandomModule, TemporaryDie}

object MockDieFactory:
  private class MockDie(numFaces: Int) extends TemporaryDie:
    override def maxFaces: Int = numFaces
    private var faces: List[CarriesResource] = List.empty
    private def isFull: Boolean = numFaces == faces.length

    private def addFace(face: CarriesResource): Unit =
      if !isFull then faces = face :: faces

    override def addFaces(addedFaces: CarriesResource*): Unit =
      addedFaces.foreach(f => this.addFace(f))

    override def roll(using randomModule: RandomModule[Int]): CarriesResource =
      faces(randomModule.randomIndex(maxFaces))

  private def oneFaceDie(playerBoard: PlayerBoard, effect: CarriesResource): TemporaryDie =
    val die = MockDie(1)
    effect match {
      case ResourceEffect(_, _, _) =>
        val face = effect.asInstanceOf[ResourceEffect]
        face.setReceiver(playerBoard)
        die.addFaces(face)
      case _ => die.addFaces(effect)
    }
    die

  def mockGoldDie(playerBoard: PlayerBoard): TemporaryDie =
    oneFaceDie(playerBoard, ResourceEffect(Gold(2)))

  def mockCopyDie(playerBoard: PlayerBoard): TemporaryDie =
    oneFaceDie(playerBoard, CopyEffect())

  def mockOptionDie(playerBoard: PlayerBoard): TemporaryDie =
    oneFaceDie(playerBoard, OptionEffect(List(Gold(3), SunCrystal(2))))

  def mockMultiplyDie(playerBoard: PlayerBoard): TemporaryDie =
    oneFaceDie(playerBoard, MultiplyEffect(3))