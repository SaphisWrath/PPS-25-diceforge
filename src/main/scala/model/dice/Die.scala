package model.dice

import model.effects.Effect
import model.utils.RandomModule

trait Die:
  def roll(using randomModule: RandomModule[Int]): Effect

  def addFace(newFace: Effect, replacedFace: Option[Effect] = None): Unit

  def faces: Seq[Effect]

  def lastEffect: Option[Effect]

object Die:
  private class BaseDie(numFaces: Int) extends Die:
    private var _faces: Seq[Effect] = Seq.empty
    private val maxFaces: Int = numFaces
    private var _lastEffect: Option[Effect] = Option.empty

    private def isFull: Boolean = numFaces == _faces.length

    override def roll(using randomModule: RandomModule[Int]): Effect =
      _lastEffect = Option(_faces(randomModule.randomIndex(maxFaces)))
      _lastEffect.get

    override def faces: Seq[Effect] = _faces

    override def addFace(newFace: Effect, replacedFace: Option[Effect]): Unit =
      if !isFull then
        _faces = _faces.+:(newFace)
      else
        replacedFace match
          case Some(oldFace) =>
            _faces = _faces.diff(Seq(oldFace))
            addFace(newFace)
          case _ => throw IllegalStateException("Max number of faces was reached but no replacedFace was provided.")

    override def lastEffect: Option[Effect] = _lastEffect

  def apply(numFaces: Int): Die = new BaseDie(numFaces)
  
  def apply(faces: Seq[Effect]): Die =
    val die = BaseDie(faces.size)
    faces.foreach(die.addFace(_, Option.empty))
    die
