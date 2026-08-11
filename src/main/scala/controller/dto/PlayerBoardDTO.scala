package controller.dto

import controller.converters.ResourceConverters.*
import model.resource.*

trait PlayerBoardDTO:
  def amountOf(resource: String): Int

  def capOf(resource: String): Option[Int]

  def resourceList: Seq[String]

object PlayerBoardDTO:
  private case class PlayerBoardDTOImpl(private val playerBoard: PlayerBoard) extends PlayerBoardDTO:
    private def getResource(resource: String): Resource = stringToResourceBuilder(resource)(0) match
      case Gold(_) => playerBoard.gold
      case SunCrystal(_) => playerBoard.sunCrystals
      case MoonCrystal(_) => playerBoard.moonCrystals
      case GloryPoint(_) => playerBoard.gloryPoints

    def amountOf(resourceName: String): Int = getResource(resourceName).amount

    def capOf(resourceName: String): Option[Int] =
      val resource = getResource(resourceName)
      resource match
        case cap: ResourceWithCap => Option(cap.maxCapacity)
        case _ => Option.empty


    override def resourceList: Seq[String] = Seq(
      resourceToString(playerBoard.gold.resource),
      resourceToString(playerBoard.sunCrystals.resource),
      resourceToString(playerBoard.moonCrystals.resource),
      resourceToString(playerBoard.gloryPoints)
    )

  def apply(board: PlayerBoard): PlayerBoardDTO = PlayerBoardDTOImpl(board)
  
  def empty: PlayerBoardDTO = PlayerBoardDTO(PlayerBoard.emptyBoard)