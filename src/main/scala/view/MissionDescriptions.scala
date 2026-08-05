package view

import controller.dto.MissionDTO

object MissionDescriptions:
  private val descMap: Map[String, String] = Map.empty
  def getDescription(missionDTO: MissionDTO): String = descMap.getOrElse(missionDTO.id, "No description found for this mission")