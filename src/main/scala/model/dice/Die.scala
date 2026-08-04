package model.dice

import model.effects.Effect
import model.utils.RandomModule

trait Die:
  def roll(using randomModule: RandomModule[Int]): Effect

  def addFaces(addedFaces: Effect*): Unit

  def faces: Seq[Effect]

object Die:
  private class BaseDie(numFaces: Int) extends Die:
    private var _faces: Seq[Effect] = Seq.empty
    private val maxFaces: Int = numFaces
    private val isFull: Boolean = numFaces == _faces.length

    private def addFace(face: Effect): Unit =
      if !isFull then _faces = _faces.+:(face)

    override def addFaces(addedFaces: Effect*): Unit =
      addedFaces.foreach(addFace)

    override def roll(using randomModule: RandomModule[Int]): Effect =
      _faces(randomModule.randomIndex(maxFaces))

    override def faces: Seq[Effect] = _faces

  def apply(numFaces: Int): Die = BaseDie(numFaces)
