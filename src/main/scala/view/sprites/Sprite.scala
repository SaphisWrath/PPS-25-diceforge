package view.sprites

import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.BackgroundRepeat.NoRepeat
import scalafx.scene.layout.{Background, BackgroundImage, BackgroundPosition, BackgroundSize}

import java.io.{File, FileInputStream}

trait Sprite:

  /**
   * @return the requested sprite as a BackgroundImage
   */
  def getSpriteAsBackground: Background

  /**
   * @return the requested Sprite as a simple image
   */
  def getSpriteAsImage: Image

  /**
   * @return the requested sprite as an ImageView
   */
  def getSpriteAsImageView: ImageView

object Sprite:
  private class BaseSprite(path: String) extends Sprite:
    override def getSpriteAsImage: Image =
      val file = File(path)
      Image(FileInputStream(file))

    override def getSpriteAsBackground: Background =
      Background(Array(BackgroundImage(getSpriteAsImage, NoRepeat, NoRepeat, BackgroundPosition.Center, BackgroundSize.Default)))

    override def getSpriteAsImageView: ImageView = ImageView(getSpriteAsImage)

  def apply(path: String): Sprite = BaseSprite(path)
