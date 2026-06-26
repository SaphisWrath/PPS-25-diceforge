package model.dice

import model.utils.RandomModule

import scala.util.Random

trait Die:
  def maxFaces: Int
  def roll(using randomModule: RandomModule[Int]): Face
  def addFaces(addedFaces: Face*): Unit

class BaseDie(numFaces: Int) extends Die:
  override def maxFaces: Int = numFaces
  var faces: List[Face] = List()
  def isFull: Boolean = numFaces == faces.length

  private def addFace(face: Face): Unit =
    if !isFull then faces = face :: faces

  override def addFaces(addedFaces: Face*): Unit =
    addedFaces.foreach(f => this.addFace(f))

  override def roll(using randomModule: RandomModule[Int]): Face =
    faces(randomModule.randomIndex(maxFaces))
