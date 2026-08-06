package model

import model.Players.Player

trait MapManager:
  def playerPositions: Map[Int, Player]
  def playerInPosition(position: Int): Option[Player]
  def movePlayer(player: Player, newPosition: Int): Unit

object MapManager:
  private class MapManagerImpl(players: Seq[Player], onThrowOut: Player => Unit) extends MapManager:
    private val startingPosition: Int = -1
    private var map: Map[Int, Player] = players.map((startingPosition, _)).toMap

    override def playerPositions: Map[Int, Player] = map

    override def playerInPosition(position: Int): Option[Player] = map.get(position)

    override def movePlayer(player: Player, newPosition: Int): Unit =
      playerInPosition(newPosition) match
        case Some(p) =>
          onThrowOut(p)
          changePosition(p, startingPosition)
        case _ =>
      changePosition(player, newPosition)

    private def changePosition(player: Player, newPosition: Int): Unit =
      map = map.filter((_, p) => p != player).updated(newPosition, player)
