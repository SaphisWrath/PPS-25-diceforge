package controller

import controller.dto.{PlayerBoardDTO, PlayerDTO}
import model.GameMatch
import model.Players.Color.{Blue, Green, Orange}
import model.Players.Player
import model.resource.PlayerBoard
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar.mock
import _root_.mock.MockPlayer

object MockGameMatches:
  def mockGameMatch: GameMatch =
    val players = Seq(
      MockPlayer("Mario", Orange, PlayerBoard(0, 0, 0, 60)),
      MockPlayer("Luigi", Green, PlayerBoard(0, 0, 0, 110)),
      MockPlayer("Toad", Blue, PlayerBoard(0, 0, 0, 85))
    )
    val boards = players.map(_.board) 

    val gameMatch = mock[GameMatch]
    when(gameMatch.players).thenReturn(players)
    gameMatch
