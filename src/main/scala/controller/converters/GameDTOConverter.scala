package controller.converters

import controller.dto.*
import model.GameMatch
import model.Players.Player
import model.effects.{ResourceEffect, Target}
import model.resource.Gold
import model.turn.TurnManagers.TurnAction.{ActivateSupport, StandardAction}

class GameDTOConverter(private val gameMatch: GameMatch):

  def activePlayerDTO: PlayerDTO = PlayerDTO(gameMatch.activePlayer)

  def nonActivePlayersDTOs: Seq[PlayerDTO] = gameMatch.nonActivePlayers.map(PlayerDTO(_))

  def playersDTOs: Seq[PlayerDTO] = gameMatch.players.map(PlayerDTO(_))

  def playerBoardDTO(playerName: String): PlayerBoardDTO = gameMatch.playerFrom(playerName) match
    case Some(player) => PlayerBoardDTO(player.board)
    case _ => PlayerBoardDTO.empty

  def playerPositionsDTO: Map[Int, PlayerDTO] = gameMatch.playerPositions.map((i, p) => i -> PlayerDTO(p))

  def playerMissions(playerName: String, extractTarget: Target => Seq[Player]): Seq[MissionDTO] = gameMatch.playerFrom(playerName) match
    case Some(player) => player.missions.map(m => MissionDTO(
      m,
      () => !m.canGet(extractTarget) || !gameMatch.isActionAvailable(ActivateSupport),
      () =>
        m.get(extractTarget)
        gameMatch.executeAction(ActivateSupport)
    ))
    case _ => Seq.empty

  def missionDTOs(extractTarget: Target => Seq[Player]): Map[Int, Seq[MissionDTO]] =
    gameMatch.missions.map((i, list) => (i, list.map(m => MissionDTO(
      m,
      () => !m.canGet(extractTarget) || !gameMatch.isActionAvailable(StandardAction),
      () => {
        gameMatch.movePlayer(gameMatch.activePlayer, i)
        m.get(extractTarget)
        gameMatch.executeAction(StandardAction)
      }
    ))))

  def recentDiceResultsDTO(playerName: String): Seq[Option[EffectDTO]] =
    gameMatch.playerFrom(playerName).get.dice.map(d =>
      if d.lastRolledEffect.isEmpty then None
      else Some(EffectDTO(d.lastRolledEffect.get))
    )

  def shopItemsDTO: Seq[ItemDTO] =
    val shop = gameMatch.shop
    shop.items
      .map(i => (i, shop.getPrice(i)))
      .sortBy(_._2.getOrElse(Gold(0)).amount)
      .map((item, cost) => ItemDTO(
        EffectDTO(item),
        EffectDTO(ResourceEffect(cost.get, Target.Self)),
        shop.getStocked(item).getOrElse(0),
        () => {
          shop.buy(item, gameMatch.activePlayer)
          gameMatch.executeAction(StandardAction)
        },
        () => shop.getStocked(item).getOrElse(0) > 0 && gameMatch.activePlayer.board.canSpend(cost.get)
      ))

  def diceDTO(playerDTO: PlayerDTO): Seq[DieDTO] =
    gameMatch.playerFrom(playerDTO.name).get.dice.map(DieDTO(_))