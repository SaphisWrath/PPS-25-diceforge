package controller

import controller.dto.{PlayerBoardDTO, PlayerDTO}
import model.GameMatch
import model.Players.Color.{Blue, Green, Orange}
import model.Players.Player
import model.resource.PlayerBoard
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar.mock

object MockGameMatches:
  def mockGameMatch: GameMatch =
    val boards = Seq(
      PlayerBoard(Player("Mario", Orange), 0, 0, 0, 60),
      PlayerBoard(Player("Luigi", Green), 0, 0, 0, 110),
      PlayerBoard(Player("Toad", Blue), 0, 0, 0, 85)
    )
    val players = boards.map(_.player)

    val gameMatch = mock[GameMatch]
    when(gameMatch.players).thenReturn(players)
    players.foreach(
      player => when(player.board).thenReturn(boards.find(_.player.name == player.name).get)
    )
    gameMatch
