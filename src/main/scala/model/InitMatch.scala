package model

enum Color:
  case Orange, Blue, Green, Black
  
object Color:
  val colorMap = Map(("Orange", Orange), ("Blue", Blue), ("Green", Green), ("Black", Black))
  def stringToColor(color: String): Option[Color] = colorMap.get(color)

type Name = String
type Player = (Name, Color)

/**
 * A factory that creates players and makes sure new players don't share
 * the same name or colors as already created players
 */
trait PlayerFactory:
  /**
   * A method that creates a new player
   *
   * @param name the name of the player
   * @param color the color of the player
   * @return an Option containing the player if the name and color assigned aren't already in use,
   *         an empty Option otherwise
   */
  def create(name: Name, color: Color): Option[Player]

class PlayerFactoryImpl extends PlayerFactory:
  private var playerList: Seq[Player] = List.empty

  def create(name: Name, color: Color): Option[Player] =
    val newPlayer = (name, color)
    if playerList.map((pName, pColor) => pName).contains(name) ||
      playerList.map((pName, pColor) => pColor).contains(color)
      then Option.empty
    else
      playerList = playerList.concat(List(newPlayer))
      Some[Player](newPlayer)
