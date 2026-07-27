package model.resource

import model.Players.Player

trait PlayerBoard:
  def player: Player
  def gold: ResourceWithCap
  def sunCrystals: ResourceWithCap
  def moonCrystals: ResourceWithCap
  def gloryPoints: Resource
  def gold_=(gold: ResourceWithCap): Unit
  def sunCrystals_=(sunCrystals: ResourceWithCap): Unit
  def moonCrystals_=(moonCrystals: ResourceWithCap): Unit
  def gloryPoints_=(gloryPoints: Resource): Unit

object PlayerBoard:
  private class PlayerBoardImpl(val player: Player,
                                var gold: ResourceWithCap,
                                var sunCrystals: ResourceWithCap,
                                var moonCrystals: ResourceWithCap,
                                var gloryPoints: Resource) extends PlayerBoard

  def apply(player: Player,
            gold: Int,
            sunCrystals: Int,
            moonCrystals: Int,
            gloryPoints: Int): PlayerBoard = PlayerBoardImpl(
    player,
    ResourceWithCap(Gold(gold), 12),
    ResourceWithCap(SunCrystal(sunCrystals), 6),
    ResourceWithCap(MoonCrystal(moonCrystals), 6),
    GloryPoint(gloryPoints)
  )

  def unapply(playerBoard: PlayerBoard): Some[(Player, Int, Int, Int, Int)] =
    Some((
      playerBoard.player,
      playerBoard.gold.amount,
      playerBoard.sunCrystals.amount,
      playerBoard.moonCrystals.amount,
      playerBoard.gloryPoints.amount
    ))

  def emptyBoard(player: Player): PlayerBoard = PlayerBoard(player,0,0,0,0)

  extension (board: PlayerBoard)
    private def updateBoard(resource: Resource, fun: (Resource, Resource) => Resource): Unit = resource match
      case Gold(_) => board.gold = ResourceWithCap(fun(board.gold.resource, resource), board.gold.maxCapacity)
      case SunCrystal(_) => board.sunCrystals = ResourceWithCap(fun(board.sunCrystals.resource, resource), board.sunCrystals.maxCapacity)
      case MoonCrystal(_) => board.moonCrystals = ResourceWithCap(fun(board.moonCrystals.resource, resource), board.moonCrystals.maxCapacity)
      case GloryPoint(_) => board.gloryPoints = fun(board.gloryPoints, resource)

    def addResource(resource: Resource): Unit = board.updateBoard(resource, _ + _)
    def takeResource(resource: Resource): Unit = board.updateBoard(resource, _ - _)