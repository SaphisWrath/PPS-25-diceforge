package controller.dto.imagepathfinder

import model.resource.{Resource, Gold, GloryPoint, SunCrystal, MoonCrystal}
import scalafx.scene.layout.BackgroundImage

trait ImagePathFinder[T]:
  val assetCommonPath = "assets/"

  /**
   *
   * @tparam T the type of model element that requires an image
   * @return
   */
  def getPath(element: T): Option[String]
  
object ImagePathFinders:
  private class ResourceImageFinder extends ImagePathFinder[Resource]:
    private val spritePath = assetCommonPath + "sprites/"
    
    override def getPath(element: Resource): Option[String] = element match
      case Gold(_) => Some(spritePath + "placeholder.png")
      case _ => None
      
  def apply(resource: Resource): ImagePathFinder[Resource] = ResourceImageFinder()