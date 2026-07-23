package controller

import controller.dto.{PlayerBoardDTO, PlayerDTO}
import model.Players.Color.{Blue, Green, Orange}
import model.Players.Player
import model.resource.PlayerBoard
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar.mock

object MockControllers:
  def mockGameController: GameController =
    val players = Seq(Player("Mario", Orange), Player("Luigi", Green), Player("Toad", Blue)).map(PlayerDTO(_))
    val boards = Seq(
      PlayerBoard(0, 0, 0, 60),
      PlayerBoard(0, 0, 0, 110),
      PlayerBoard(0, 0, 0, 85)
    )

    val boardMap = players.map(_.name).zip(boards).toMap

    val controller = mock[GameController]
    when(controller.players).thenReturn(players)
    players.foreach(player => when(controller.playerBoard(player)).thenReturn(PlayerBoardDTO(boardMap(player.name))))
    controller
