package view

import controller.GameController
import model.Players.Color.{Blue, Orange}
import model.Players.Player
import scalafx.application.JFXApp3
import view.scenes.BoardScene

object BoardSceneTest extends JFXApp3:
  override def start(): Unit = {
    GameController.init(
      playerList = Seq(Player("Paul", Orange), Player("Paulo", Blue))
    )
    stage = TestStageSetup(BoardScene()).stage
  }

