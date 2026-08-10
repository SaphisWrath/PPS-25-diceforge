package view

import controller.dto.MissionDTO

object MissionDescriptions:
  private val descMap: Map[String, (String, String)] = Seq(
    ("ferryman", ("Tragettatore", "Ottieni 12 Punti Vittoria")),
    ("gorgon", ("Gorgone", "Ottieni 14 Punti Vittoria")),
    ("hydra", ("Idra", "Ottieni 26 Punti Vittoria")),
    ("smith_chest", ("Forziere del fabbro", "Ottieni 2 Punti Vittoria e aumenta la tua capacità massima di 4 Oro, 3 Cristalli Solari, e 3 Cristalli Lunari")),
    ("satyr", ("Satiri", "Ottieni 6 Punti Vittoria; tutti gli altri giocatori tirano i propri dadi senza appicarne gli effetti, poi scegli quali effetti applicare per te stesso")),
    ("helmet", ("Elmo dell'invisibilità", "Ottieni 4 Punti Vittoria e ottieni una faccia \"Risultato per 3\" da applicare a uno dei tuoi dadi")),
    ("spirits", ("Spiriti Selvaggi", "Ottieni 2 Punti Vittoria, 3 Oro, e 3 Cristalli Lunari")),
    ("minotaur", ("Minotauro", "Ottieni 8 Punti Vittoria; tutti gli altri giocatori lanciano i loro dadi, ma perdono le risorse determinate dal lancio dei dadi invece di guadagnarle")),
    ("scorpion", ("Scorpione", "Ottieni 8 Punti Vittoria; tira entrambi i tuoi dadi due volte e ottieni i risultati")),
    ("mirror", ("Specchio dell'abisso", "Ottieni 10 Punti Vittoria e ottieni una faccia \"Copia\" da applicare a uno dei tuoi dadi")),
    ("sphinx", ("Sfinge", "Ottieni 10 Punti Vittoria, poi tiri uno dei tuoi dadi 4 volte e ne ottieni i risultati")),
    ("smith_hammer", ("Martello del fabbro", "Spendi 12 Oro per ottenere 20 Punti Vittoria")),
    ("silver_doe", ("Cerva d'argento", "Ottieni 2 Punti Vittoria; durante il tuo turno puoi tirare uno dei tuoi dadi e applicarne gli effetti")),
    ("elder", ("Anziano", "Spendi 3 Oro per ottenere 4 Punti Vittoria")),
    ("owl", ("Gufo del guardiano", "Ottieni 4 Punti Vittoria; durante il tuo turno puoi ottenere a scelta 1 Oro, 1 Cristallo Solare, o 1 Cristallo Lunare"))
  ).toMap

  private val notFoundMessage = ("Untitled", "No description found for this mission")

  def getTitle(missionDTO: MissionDTO): String =
    descMap.getOrElse(missionDTO.id, notFoundMessage)._1

  def getDescription(missionDTO: MissionDTO): String =
    descMap.getOrElse(missionDTO.id, notFoundMessage)._2