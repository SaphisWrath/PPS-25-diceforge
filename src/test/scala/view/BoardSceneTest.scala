package view

import controller.{GameController, Navigator}
import controller.dto.MissionDTO
import model.Players.Color.{Blue, Orange}
import model.Players.Player
import model.effects.ResourceEffect
import model.missions.BaseMission
import model.resource.{Gold, MoonCrystal, SunCrystal}
import scalafx.application.JFXApp3
import view.scenes.BoardScene

object BoardSceneTest extends JFXApp3:
  enum Scene:
    case MainMenu
    case MatchInit
    case Board
    case MatchEnd
    
  class MockNavigator(private var _scene: Scene) extends Navigator:
    import Scene.*
    def currentScene: Scene = _scene
    
    override def navigateToMainMenu(): Unit = _scene = MainMenu

    override def navigateToMatchInit(): Unit = _scene = MatchInit

    override def navigateToBoard(): Unit = _scene = Board

    override def navigateToMatchEnd(): Unit = _scene = MatchEnd
  
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
    GameController.init(
      playerList = Seq(Player("Paul", Orange), Player("Paulo", Blue))
    )
    GameController._missions = placeholderMissions
    stage = TestStageSetup(BoardScene(GameController, MockNavigator(Scene.Board))).stage
  }

