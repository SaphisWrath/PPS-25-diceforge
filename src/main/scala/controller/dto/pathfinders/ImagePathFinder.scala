package controller.dto.pathfinders

import model.effects.{CopyEffect, Effect, ResourceEffect, MultiplyEffect}
import model.resource.*
import scalafx.scene.layout.BackgroundImage

import java.io.File

trait ImagePathFinder[T]:
  val systemSeparator: String = System.getProperty("file.separator")
  val assetCommonPath: String = "src" + systemSeparator + "main" + systemSeparator + "resources" + systemSeparator

  /**
   *
   * @tparam T the type of model element that requires an image
   * @return
   */
  def getPath(element: T): String

object ImagePathFinders:

  def findImagePath[T: ImagePathFinder](element: T): String = summon[ImagePathFinder[T]].getPath(element)

  given ImagePathFinder[Resource] with
    private val spritePath = assetCommonPath + "sprites" + systemSeparator

    override def getPath(element: Resource): String = element match
      case Gold(_) => spritePath + "gold.png"
      case GloryPoint(_) => spritePath + "glory_point.png"
      case SunCrystal(_) => spritePath + "sun.png"
      case MoonCrystal(_) => spritePath + "moon.png"
      case _ => spritePath + "placeholder.png"

  given ImagePathFinder[Effect] with
    private val spritePath = assetCommonPath + "sprites" + systemSeparator

    override def getPath(element: Effect): String = element match
      case ResourceEffect(resource, _, _) => summon[ImagePathFinder[Resource]].getPath(resource)
      case MultiplyEffect(_) => spritePath + "multiply.png"
      case t: CopyEffect => spritePath + "copy.png"
      case _ =>  spritePath + "placeholder.png"
  