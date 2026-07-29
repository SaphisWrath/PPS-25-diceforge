package controller.dto.pathfinders

import model.resource.{Resource, Gold, GloryPoint, SunCrystal, MoonCrystal}
import scalafx.scene.layout.BackgroundImage

trait ImagePathFinder[T]:
  val assetCommonPath = "assets/"

  /**
   *
   * @tparam T the type of model element that requires an image
   * @return
   */
  def getPath(element: T): String

object ImagePathFinders:

  def findImagePath[T: ImagePathFinder](element: T): String = summon[ImagePathFinder[T]].getPath(element)

  given ImagePathFinder[Resource] with
    private val spritePath = assetCommonPath + "sprites/"

    override def getPath(element: Resource): String = element match
      case Gold(_) => spritePath + "placeholder.png"
      case _ => spritePath + "placeholder.jpeg"