package view

import controller.ViewState.{Board, MatchEnd}
import controller.{ControllerStage, GameController, Navigator}
import controller.dto.MissionDTO
import model.GameMatch
import model.Players.Color.{Blue, Orange}
import model.Players.Player
import model.effects.ResourceEffect
import model.missions.BaseMission
import model.resource.{Gold, MoonCrystal, SunCrystal}
import scalafx.application.JFXApp3
import view.scenes.BoardScene
import org.mockito.Mockito.*
import _root_.mock.MockControllerStage

object BoardSceneTest extends JFXApp3:

  override def start(): Unit = {
    val cost: List[ResourceEffect] = (ResourceEffect(SunCrystal(3)), ResourceEffect(MoonCrystal(3))).toList
    val reward: List[ResourceEffect] = List(ResourceEffect(Gold(3)))
    val placeholderMission = MissionDTO(BaseMission(reward, cost))
    val placeholderMissions = List(
      0 -> List(placeholderMission, placeholderMission),
      1 -> List(placeholderMission, placeholderMission),
      2 -> List(placeholderMission, placeholderMission),
      3 -> List(placeholderMission, placeholderMission),
      4 -> List(placeholderMission, placeholderMission),
      5 -> List(placeholderMission, placeholderMission),
      6 -> List(placeholderMission, placeholderMission, placeholderMission)
    ).toMap
    val controller = GameController(GameMatch(Seq(Player("Paul", Orange), Player("Paulo", Blue))))
    controller.missions = placeholderMissions
    stage = TestStageSetup(BoardScene(controller, MockControllerStage(Board))).stage
  }

