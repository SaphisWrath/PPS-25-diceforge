package controller

import controller.dto.{PlayerBoardDTO, PlayerDTO}
import model.Players.Color.{Blue, Green, Orange}
import model.Players.Player
import model.resource.PlayerBoard
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar.mock

object MockControllers:
  def mockGameController: GameController =
    val boards = Seq(
      PlayerBoard(Player("Mario", Orange), 0, 0, 0, 60),
      PlayerBoard(Player("Luigi", Green), 0, 0, 0, 110),
      PlayerBoard(Player("Toad", Blue), 0, 0, 0, 85)
    )
    val players = boards.map(b => PlayerDTO(b.player))
    val boardMap = players.map(_.name).zip(boards).toMap

    val controller = mock[GameController]
    when(controller.players).thenReturn(players)
    players.foreach(
      player => when(controller.playerBoard(player)).thenReturn(PlayerBoardDTO(boards.find(_.player.getName == player.name).get))
    )
    controller
