package view

import controller.dto.MissionDTO
import LanguageStrings.ResourceStrings.*

object MissionDescriptions:
  private val descMap: Map[String, (String, String)] = Seq(
    ("ferryman", ("Traghettatore", s"Ottieni 12 $gloryPoint")),
    ("gorgon", ("Gorgone", s"Ottieni 14 $gloryPoint")),
    ("hydra", ("Idra", s"Ottieni 26 $gloryPoint")),
    ("smith_chest", ("Forziere del fabbro", s"Ottieni 2 $gloryPoint e aumenta la tua capacità massima di 4 $gold, 3 $sunCrystal, e 3 $moonCrystal")),
    ("satyr", ("Satiri", s"Ottieni 6 $gloryPoint; tutti gli altri giocatori tirano i propri dadi senza appicarne gli effetti, poi scegli quali effetti applicare per te stesso")),
    ("helmet", ("Elmo dell'invisibilità", s"Ottieni 4 $gloryPoint e ottieni una faccia \"Risultato per 3\" da applicare a uno dei tuoi dadi")),
    ("spirits", ("Spiriti Selvaggi", s"Ottieni 2 $gloryPoint, 3 $gold, e 3 $moonCrystal")),
    ("minotaur", ("Minotauro", s"Ottieni 8 $gloryPoint; tutti gli altri giocatori lanciano i loro dadi, ma perdono le risorse determinate dal lancio dei dadi invece di guadagnarle")),
    ("scorpion", ("Scorpione", s"Ottieni 8 $gloryPoint; tira entrambi i tuoi dadi due volte e ottieni i risultati")),
    ("mirror", ("Specchio dell'abisso", s"Ottieni 10 $gloryPoint e ottieni una faccia \"Copia\" da applicare a uno dei tuoi dadi")),
    ("sphinx", ("Sfinge", s"Ottieni 10 $gloryPoint, poi tiri uno dei tuoi dadi 4 volte e ne ottieni i risultati")),
    ("smith_hammer", ("Martello del fabbro", s"Spendi 12 $gold per ottenere 17 $gloryPoint")),
    ("silver_doe", ("Cerva d'argento", s"Ottieni 2 $gloryPoint; durante il tuo turno puoi tirare uno dei tuoi dadi e applicarne gli effetti")),
    ("elder", ("Anziano", s"Spendi 3 $gold per ottenere 4 $gloryPoint")),
    ("owl", ("Gufo del guardiano", s"Ottieni 4 $gloryPoint; durante il tuo turno puoi ottenere a scelta 1 $gold, 1 $sunCrystal, o 1 $moonCrystal"))
  ).toMap

  private val notFoundMessage = ("Untitled", "No description found for this mission")

  def getTitle(missionDTO: MissionDTO): String =
    descMap.getOrElse(missionDTO.id, notFoundMessage)._1

  def getDescription(missionDTO: MissionDTO): String =
    descMap.getOrElse(missionDTO.id, notFoundMessage)._2