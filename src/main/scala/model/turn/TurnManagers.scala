package model.turn

import model.turn.TurnManagers.TurnStep.*


object TurnManagers:
  enum TurnAction(private val transitions: Map[TurnStep, TurnStep]):
    case CompleteDiceThrow extends TurnAction(Map(StartStep -> SupportStep))
    case StandardAction extends TurnAction(Map(MainActionStep -> PostMainActionStep, ExtraActionStep -> PostExtraActionStep))
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

  case class TurnManager(
                          currentStep: TurnStep,
                          private val actionRequirements: PartialFunction[TurnAction, Boolean] = PartialFunction.empty,
                          private val consecutiveActions: PartialFunction[TurnAction, TurnAction]  = PartialFunction.empty,
                        ):
    def executeAction(turnAction: TurnAction): Option[TurnManager] =
      if isActionAvailable(turnAction) then
        val newTm = this.copy(turnAction.getTransition(currentStep).get)
        consecutiveActions.lift(turnAction) match
          case Some(ta) if newTm.isActionAvailable(ta) => newTm.executeAction(ta)
          case _ => Option(newTm)
      else
        Option.empty

    def isActionAvailable(turnAction: TurnAction): Boolean =
      turnAction.isAvailable(currentStep) && actionRequirements.applyOrElse(turnAction, _ => true)