package model.dice

trait Die:
  def maxFaces: Int
  def roll(): Face
  def addFaces(addedFaces: Face*): Unit

class BaseDie(numFaces: Int) extends Die:
  override def maxFaces: Int = numFaces
  var faces: List[Face] = List()
  def isFull: Boolean = numFaces == faces.length

  private def addFace(face: Face): Unit =
    if !isFull then faces = face :: faces

  override def addFaces(addedFaces: Face*): Unit =
    addedFaces.foreach(f => this.addFace(f))

  override def roll(): Face = ???
