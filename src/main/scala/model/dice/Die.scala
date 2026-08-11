package model.dice

import model.effects.Effect
import model.utils.RandomModule

trait Die:
  /**
   * Rolls the die and returns the resulting face
   * 
   * @param randomModule the module utilized for generating a random number
   * @return the result of the roll
   */
  def roll(using randomModule: RandomModule[Int]): Effect

  /**
   * Adds a face to the die, returning an exception if the replaced face was not provided and the die is full
   * @param newFace the new face
   * @param replacedFace the face that will be replaced by newFace
   */
  def addFace(newFace: Effect, replacedFace: Option[Effect] = None): Unit

  /**
   * Adds a face from the internal queue of new faces, requiring the face that will be replaced.
   * 
   * @param replacedFace the face that will be replaced by the top of the new faces queue
   */
  def addFaceFromQueue(replacedFace: Effect): Unit

  /**
   * @return All the faces of the die
   */
  def faces: Seq[Effect]

  /**
   * @return the result of the most recent roll, if there is one
   */
  def lastRolledEffect: Option[Effect]

  /**
   * Adds a new face to the queue of faces that should be added to the die
   * @param nextFace the new face
   */
  def setQueueFace(nextFace: Effect): Unit

object Die:
  private class BaseDie(numFaces: Int) extends Die:
    private var _faces: Seq[Effect] = Seq.empty
    private val maxFaces: Int = numFaces
    private var _lastEffect: Option[Effect] = None
    private var _queueEffect: Option[Effect] = None

    private def isFull: Boolean = numFaces == _faces.length

    override def roll(using randomModule: RandomModule[Int]): Effect =
      _lastEffect = Option(_faces(randomModule.randomIndex(_faces.length)))
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

    override def lastRolledEffect: Option[Effect] = _lastEffect

    override def setQueueFace(nextFace: Effect): Unit = _queueEffect = Some(nextFace)

    override def addFaceFromQueue(replacedFace: Effect): Unit =
      if _queueEffect.isEmpty
      then throw IllegalStateException("Can't add a face from empty queue.")
      else addFace(_queueEffect.get, Some(replacedFace))
      _queueEffect = None

  def apply(numFaces: Int): Die = new BaseDie(numFaces)
  
  def apply(faces: Seq[Effect]): Die =
    val die = BaseDie(faces.size)
    faces.foreach(die.addFace(_, None))
    die
