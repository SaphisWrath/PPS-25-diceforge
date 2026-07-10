package controller

import model.Players.Color.{Black, Blue, Green, Orange}
import model.Players.{Color, Player}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class GameControllerTest extends AnyFlatSpec with should.Matchers:
  enum ExPlayers(private val _name: String, private val _color: Color) extends Player:
    case Player1 extends ExPlayers("P1", Green)
    case Player2 extends ExPlayers("P2", Blue)
    case Player3 extends ExPlayers("P3", Black)
    case Player4 extends ExPlayers("P4", Orange)
    override def getName: String = _name
    override def getColor: Color = _color

  def initGame(playerNum: Int): Seq[Player] =
    GameController.reset()
    val players = ExPlayers.values.take(playerNum).toSeq
    GameController.init(players)
    players

  "A Game" should "be empty after a reset" in:
    GameController.reset()
    GameController.activePlayer.isEmpty should be (true)
    GameController.currentRound should be (1)

  it should "be able to start" in:
    val players = initGame(2)
    GameController.activePlayer.nonEmpty should be (true)
    players should contain (GameController.activePlayer.get)

  it should "let you check non active Players" in:
    val players = initGame(2)
    val nonActivePlayers = players.filter(_.equals(GameController.activePlayer.get))
    nonActivePlayers should be (GameController.nonActivePlayerList)

  it should "let you go to the next turn" in:
    val players = initGame(2)
    val activePlayer = GameController.activePlayer
    GameController.nextTurn()
    GameController.activePlayer should not be activePlayer

  it should "go to the next round after everybody took a turn" in:
    val players = initGame(2)
    val oldRound = GameController.currentRound
    players.foreach(_ => GameController.nextTurn())
    GameController.currentRound should not be oldRound

  it should "end when the maximum number of rounds is reached" in:
    val players = initGame(2)
    Range(0, GameController.maxNumberOfRounds).foreach(_ =>
      players.foreach(_ => GameController.nextTurn())
    )
    GameController.isGameEnded should be (true)