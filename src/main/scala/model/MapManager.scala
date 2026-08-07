package model

import model.Players.Player
//TODO Add ScalaDoc
trait MapManager:
  def playerPositions: Map[Int, Player]

  def playerInPosition(position: Int): Option[Player]

  def movePlayer(player: Player, newPosition: Int): Unit

object MapManager:
  private class MapManagerImpl(onMove: () => Unit, onThrowOut: Player => Unit) extends MapManager:
    private var map: Map[Int, Player] = Map.empty

    override def playerPositions: Map[Int, Player] = map

    override def playerInPosition(position: Int): Option[Player] = map.get(position)

    override def movePlayer(player: Player, newPosition: Int): Unit =
      playerInPosition(newPosition) match
        case Some(playerToBeRemoved) if player != playerToBeRemoved =>
          onThrowOut(playerToBeRemoved)
          map = map.filter((_, p) => p != playerToBeRemoved)
        case _ =>
      map = map.filter((_, p) => p != player).updated(newPosition, player)
      onMove()

  def apply(onMove: () => Unit, onThrowOut: Player => Unit): MapManager = MapManagerImpl(onMove, onThrowOut)