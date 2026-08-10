package controller.dto.pathfinders

import model.effects.ThrowEffects.{ThrowAllDice, ThrowOneDie}
import model.effects.{CopyEffect, Effect, GrantFaceEffect, MultiplyEffect, ResourceEffect, SubtractThrow, UpdateCapacityEffect}
import model.resource.*
import scalafx.scene.layout.BackgroundImage

import java.io.File

trait ImagePathFinder[T]:
  val systemSeparator: String = "/"

  /**
   * Returns the sprite path, formatted for jar navigation, of the selected element
   *
   * @param element the element whose sprite we have to find
   * @return the sprite path
   */
  def getPath(element: T): String

object ImagePathFinders:

  def findImagePath[T: ImagePathFinder](element: T): String = summon[ImagePathFinder[T]].getPath(element)

  given ImagePathFinder[Resource] with
    private val spritePath = systemSeparator + "sprites" + systemSeparator

    override def getPath(element: Resource): String = element match
      case Gold(_) => spritePath + "gold.png"
      case GloryPoint(_) => spritePath + "glory_point.png"
      case SunCrystal(_) => spritePath + "sun.png"
      case MoonCrystal(_) => spritePath + "moon.png"
      case _ => spritePath + "placeholder.png"

  given ImagePathFinder[Effect] with
    private val spritePath = systemSeparator + "sprites" + systemSeparator

    override def getPath(element: Effect): String = element match
      case t: ResourceEffect => summon[ImagePathFinder[Resource]].getPath(t.resource)
      case t: MultiplyEffect => spritePath + "multiply.png"
      case t: CopyEffect => spritePath + "copy.png"
      case t: SubtractThrow => spritePath + "throw_subtract.png"
      case t: UpdateCapacityEffect => summon[ImagePathFinder[Resource]].getPath(t.resource)
      case t: ThrowOneDie => spritePath + "throw_one.png"
      case t: ThrowAllDice => spritePath  + "throw_all.png"
      case t: GrantFaceEffect => summon[ImagePathFinder[Effect]].getPath(t.newFace)
      case _ =>  spritePath + "placeholder.png"
  