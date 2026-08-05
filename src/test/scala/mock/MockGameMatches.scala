package mock

import _root_.mock.MockPlayer
import model.GameMatch
import model.Players.Color.{Blue, Green, Orange}
import model.resource.PlayerBoard
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar.mock

object MockGameMatches:
  private def mockPlayers(gloryPoints: Int*): Seq[MockPlayer] =
    Seq(
      MockPlayer("Mario", Orange, PlayerBoard(0, 0, 0, gloryPoints(0))),
      MockPlayer("Luigi", Green, PlayerBoard(0, 0, 0, gloryPoints(1))),
      MockPlayer("Toad", Blue, PlayerBoard(0, 0, 0, gloryPoints(2)))
    )

  private def mockMatch(players: Seq[MockPlayer]): GameMatch =
    val boards = players.map(_.board)
    val gameMatch = mock[GameMatch]
    when(gameMatch.players).thenReturn(players)
    gameMatch

  def mockGameMatch: GameMatch =
    mockMatch(mockPlayers(60, 110, 85))

  def mockGameMatchDraw: GameMatch =
    mockMatch(mockPlayers(75, 90, 90))