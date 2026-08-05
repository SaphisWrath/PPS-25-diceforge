package model.turn

import model.turn.TurnManagers.TurnStep.{ExtraActionStep, MainActionStep, PostExtraActionStep, PostMainActionStep, StartStep, SupportStep}

object TurnManagers:
  enum TurnAction(private val transitions: Map[TurnStep, TurnStep]):
    case CompleteMission extends TurnAction(Map(MainActionStep -> PostMainActionStep, ExtraActionStep -> PostExtraActionStep))
    case BuyFace extends TurnAction(Map(MainActionStep -> PostMainActionStep, ExtraActionStep -> PostExtraActionStep))
    case ActivateSupport extends TurnAction(Map(SupportStep -> SupportStep))
    case EndSupport extends TurnAction(Map(SupportStep -> MainActionStep))
    case BuyExtraAction extends TurnAction(Map(PostMainActionStep -> ExtraActionStep))
    case EndTurn extends TurnAction(TurnStep.values.filter(_ != StartStep).map((_, StartStep)).toMap)
    
    def isAvailable(step: TurnStep): Boolean = transitions.contains(step)
    
    def getTransition(step: TurnStep): Option[TurnStep] = transitions.get(step)
    
  enum TurnStep:
    case StartStep
    case SupportStep
    case MainActionStep
    case PostMainActionStep
    case ExtraActionStep
    case PostExtraActionStep

  case class TurnManager(currentStep: TurnStep):
    def executeAction(turnAction: TurnAction): TurnManager =
      turnAction.getTransition(currentStep).getOrElse(currentStep).move()
      
    def isActionAvailable(turnAction: TurnAction): Boolean = turnAction.isAvailable(currentStep)
    
    extension (step: TurnStep)
      private def move(): TurnManager = this.copy(currentStep= step)