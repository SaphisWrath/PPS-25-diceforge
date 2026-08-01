package controller

import controller.ViewPublishers.Context.*
import controller.ViewPublishers.ViewPublisher
import controller.dto.{MissionDTO, PlayerBoardDTO, PlayerDTO}
import model.GameMatch
import model.Players.Player
import model.effects.Target
import model.effects.Target.{All, Others, Self}
import model.resource.{PlayerBoard, SunCrystal}
import model.utils.ValueProperty

trait GameController:
  /**
   * @return the sequence of current players
   */
  def players: Seq[PlayerDTO]

  /**
   * @return the missions in play, already sorted into their respective cells
   */
  def missions: Map[Int, Seq[MissionDTO]]

  /**
   * @return the current active player
   */
  def activePlayer: PlayerDTO

  /**
   * @return the sequence of all players that are not the active player
   */
  def nonActivePlayerList: Seq[PlayerDTO]

  /**
   * @param player the player proprietary of the board
   * @return the currentPlayerBoard of the given player
   */
  def playerBoard(player: PlayerDTO): PlayerBoardDTO = playerBoard(player.name)

  /**
   * @param playerName the name of the proprietary of the board
   * @return the currentPlayerBoard of the given player
   */
  def playerBoard(playerName: String): PlayerBoardDTO

  /**
   * @param player the player proprietary of the missions
   * @return the missions obtained by the given player
   */
  def playerMissions(player: PlayerDTO): Seq[MissionDTO] = playerMissions(player.name)

  /**
   * @param playerName the name of the proprietary of the missions
   * @return the missions obtained by the given player
   */
  def playerMissions(playerName: String): Seq[MissionDTO]
  
  /**
   * Notify the game to go to the next turn
   */
  def nextTurn(): Unit

  /**
   * @return the current round number
   */
  def currentRound: Int

  /**
   * @return true if the game ended
   */
  def isGameEnded: Boolean

  /**
   * @return the maximum number of rounds of the currently initialized game
   */
  def maxNumberOfRounds: Int

  /**
   * @return true if the player already took his action, false otherwise
   */
  def hasTurnActionBeenTaken: Boolean

  def hasExtraActionBeenBought: Boolean

  def buyExtraAction(): Unit

object GameController:
  private class GameControllerImpl(private val gameMatch: GameMatch) extends GameController:

    private val _hasTurnActionBeenTaken: ValueProperty[Boolean] =
      ValueProperty(
        false,
        (_, _) => ViewPublisher.notify(ActionContext)
      )

    private val _hasExtraActionBeenBought: ValueProperty[Boolean] =
      ValueProperty(
        false,
        (_, newVal) => if newVal then ViewPublisher.notify(ExtraActionContext)
      )

    override def missions: Map[Int, Seq[MissionDTO]] =
      gameMatch.missions.map((i, list) => (i, list.map(m => MissionDTO(
        m,
        () => !m.canGet(extractTarget) || _hasTurnActionBeenTaken.value,
        () => {
          m.get(extractTarget)
          _hasTurnActionBeenTaken.value = true
          ViewPublisher.notify(ResourceContext)
          ViewPublisher.notify(MissionBoughtContext)
        }
      ))))
      
    override def playerMissions(playerName: String): Seq[MissionDTO] =
      gameMatch.playerFrom(playerName) match
        case Some(player) => player.missions.map(m => MissionDTO(
          m,
          () => !m.canGet(extractTarget),
          () =>
            m.get(extractTarget)
            ViewPublisher.notify(ResourceContext)
        ))
        case _ => Seq.empty

    private def extractTarget(target: Target): Seq[Player] = target match
      case Self => Seq(gameMatch.activePlayer)
      case All => gameMatch.players
      case Others => gameMatch.nonActivePlayers

    override def players: Seq[PlayerDTO] = gameMatch.players.map(PlayerDTO(_))

    override def activePlayer: PlayerDTO = PlayerDTO(gameMatch.activePlayer)

    override def nonActivePlayerList: Seq[PlayerDTO] = gameMatch.nonActivePlayers.map(PlayerDTO(_))

    override def playerBoard(playerName: String): PlayerBoardDTO = gameMatch.playerFrom(playerName) match
      case Some(player) => PlayerBoardDTO(player.board)
      case _ => throw IllegalArgumentException(s"$playerName does not correspond to any player") //TODO handle it better

    override def playerBoard(player: PlayerDTO): PlayerBoardDTO = playerBoard(player.name)

    override def nextTurn(): Unit =
      gameMatch.nextTurn()
      _hasTurnActionBeenTaken.value = false
      _hasExtraActionBeenBought.value = false
      ViewPublisher.notify(TurnChangeContext)

    override def currentRound: Int = gameMatch.currentRound + 1

    override def isGameEnded: Boolean = gameMatch.isGameEnded

    override def maxNumberOfRounds: Int = gameMatch.maxNumberOfRounds

    override def hasTurnActionBeenTaken: Boolean = _hasTurnActionBeenTaken.value

    override def hasExtraActionBeenBought: Boolean = _hasExtraActionBeenBought.value

    override def buyExtraAction(): Unit =
      val board = gameMatch.activePlayer.board
      if board.sunCrystals.amount >= 2 && !_hasExtraActionBeenBought.value then
        board.sunCrystals = board.sunCrystals - SunCrystal(2)
        _hasExtraActionBeenBought.value = true
        _hasTurnActionBeenTaken.value = false

  def apply(gameMatch: GameMatch): GameController = GameControllerImpl(gameMatch)


