package model

import model.Players.Player

/** Interface responsible for managing the positions of the players and their movement
 *
 */
trait MapManager:
  /** Map of the player positions
   *
   * The Map contains only the indexes where the players are
   *
   * @return A Map of the player positions
   */
  def playerPositions: Map[Int, Player]

  /** Return the player in the given position, if any
   *
   * @param position the position we want to check
   * @return the Option of the [[Player]] or an empty Option if the position is empty
   */
  def playerInPosition(position: Int): Option[Player]

  /** Move a player from his current position to the new position
   *
   * @param player      The [[Player]] to move
   * @param newPosition The destination of the player
   */
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