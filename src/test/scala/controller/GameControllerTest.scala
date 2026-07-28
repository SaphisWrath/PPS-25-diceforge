package controller

import controller.dto.{PlayerBoardDTO, PlayerDTO}
import model.Players.Color.{Black, Blue, Green, Orange}
import model.Players.{Color, Player}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import view.LanguageStrings.ResourceStrings.*

class GameControllerTest extends AnyFlatSpec with should.Matchers:
  enum ExPlayers(private val _name: String, private val _color: Color) extends Player:
    case Player1 extends ExPlayers("P1", Green)
    case Player2 extends ExPlayers("P2", Blue)
    case Player3 extends ExPlayers("P3", Black)
    case Player4 extends ExPlayers("P4", Orange)
    override def getName: String = _name
    override def getColor: Color = _color

  var gameController = GameController(Seq.empty)

  def initGame(playerNum: Int): Seq[PlayerDTO] =
    val players = ExPlayers.values.take(playerNum).toSeq
    gameController = GameController(players)
    players.map(PlayerDTO(_))

  "A Game" should "be initialized" in:
    val players = initGame(2)
    gameController.players should not be empty
    players should contain (gameController.activePlayer)
    gameController.currentRound should be (1)

  it should "let you check non active Players" in:
    val players = initGame(2)
    val nonActivePlayers = players.filter(!_.equals(gameController.activePlayer))
    nonActivePlayers should be (gameController.nonActivePlayerList)

  it should "let you go to the next turn" in:
    val players = initGame(2)
    val activePlayer = gameController.activePlayer
    gameController.nextTurn()
    gameController.activePlayer should not be activePlayer

  it should "go to the next round after everybody took a turn" in:
    val players = initGame(2)
    val oldRound = gameController.currentRound
    players.foreach(_ => gameController.nextTurn())
    gameController.currentRound should not be oldRound

  it should "end when the maximum number of rounds is reached" in:
    val players = initGame(2)
    Range(0, gameController.maxNumberOfRounds).foreach(_ =>
      players.foreach(_ => gameController.nextTurn())
    )
    gameController.isGameEnded should be (true)

  it should "set the Player Board" in:
    val players = initGame(2)
    val playerBoard: PlayerBoardDTO = gameController.playerBoard(players.head)
    playerBoard.amountOf(gold) should be (0)
    playerBoard.amountOf(sunCrystal) should be(0)
    playerBoard.amountOf(moonCrystal) should be(0)
    playerBoard.amountOf(gloryPoint) should be(0)

    playerBoard.capOf(gold) should be (Option(12))
    playerBoard.capOf(sunCrystal) should be (Option(6))
    playerBoard.capOf(moonCrystal) should be (Option(6))
    playerBoard.capOf(gloryPoint) should be (Option.empty)
