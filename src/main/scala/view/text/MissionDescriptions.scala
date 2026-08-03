package view.text

object MissionDescriptions:
  private val descMap: Map[String, String] = Map.empty
  def getDescription(id: String): String = descMap.getOrElse(id, "No description found for this mission")