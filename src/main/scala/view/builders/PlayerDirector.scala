package view.builders

import scalafx.scene.paint.Color

class PlayerDirector(playerName: String, playerColor: Color):
  def createActivePlayerBox(builder: PlayerBoxBuilder): Unit =
    builder.reset()
    builder.buildName(playerName)
    builder.buildToken(playerColor)
    Seq("Oro", "Cristalli Solari", "Cristalli Lunari") //TODO
      .foreach(builder.buildResource)
    builder.buildDiceTracker()
