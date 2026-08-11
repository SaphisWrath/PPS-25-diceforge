package controller.dto.pathfinders

import java.nio.charset.StandardCharsets
import scala.io.Source

object Paths:
  private val systemSeparator: String = "/"
  val spritePath: String = systemSeparator + "sprites" + systemSeparator
  val textPath: String = systemSeparator + "text" + systemSeparator

  def getTextContentsAsString(filename: String): String =
    val resourceName = textPath + filename

    val inputStream =
      Option(getClass.getResourceAsStream(resourceName))
        .getOrElse {
          throw new IllegalArgumentException(
            s"Resource not found: $resourceName"
          )
        }

    val source = Source.fromInputStream(
      inputStream,
      StandardCharsets.UTF_8.name()
    )

    try source.mkString
    finally source.close()

