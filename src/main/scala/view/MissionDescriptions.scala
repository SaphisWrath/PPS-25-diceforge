package view

import controller.dto.MissionDTO

object MissionDescriptions:
  private val descMap: Map[String, String] = Seq(
    ("Tragettatore", "Ottieni 12 Punti Vittoria"),
    ("Gorgone", "Ottieni 14 Punti Vittoria"),
    ("Idra", "Ottieni 26 Punti Vittoria"),
    ("Forziere del fabbro", "Ottieni 2 Punti Vittoria e aumenta la tua capacità massima di 4 Oro, 3 Cristalli Solari, e 3 Cristalli Lunari"),
    ("Satiri", "Ottieni 6 Punti Vittoria; tutti gli altri giocatori tirano i propri dadi senza appicarne gli effetti, poi scegli quali effetti applicare pre te stesso"),
    ("Elmo dell'invisibilità", "Ottieni 4 Punti Vittoria e ottieni una faccia \"Risultato per 3\" da applicare a uno dei tuoi dadi"),
    ("Spiriti Selvaggi", "Ottieni 2 Punti Vittoria, 3 Oro, e 3 Cristalli Lunari"),
    ("Minotauro", "Ottieni 8 Punti Vittoria; tutti gli altri giocatori lanciano i loro dadi, ma perdono le risorse determinate dal lancio dei dadi invece di guadagnarle"),
    ("Specchio dell'abisso", "Ottieni 10 Punti Vittoria e ottieni una faccia \"Copia\" da applicare a uno dei tuoi dadi"),
    ("Sfinge", "Ottieni 10 Punti Vittoria, poi tiri uno dei tuoi dadi 4 volte e ne ottieni i risultati"),
    ("Martello del fabbro", "Spendi 12 Oro per ottenere 20 Punti Vittoria"),
    ("Cerva d'argento", "Ottieni 2 Punti Vittoria; durante il tuo turno puoi tirare uno dei tuoi dadi e applicarne gli effetti"),
    ("Anziano", "Spendi 3 Oro per ottenere 4 Punti Vittoria"),
    ("Gufo del guardiano", "Ottieni 4 Punti Vittoria; durante il tuo turno puoi ottenere a scelta 1 Oro, 1 Cristallo Solare, o 1 Cristallo Lunare")
  ).toMap

  def getDescription(missionDTO: MissionDTO): String =
    descMap.getOrElse(missionDTO.id, "No description found for this mission")