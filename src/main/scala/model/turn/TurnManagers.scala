package model.turn

import model.turn.TurnManagers.TurnStep.{ExtraActionStep, MainActionStep, StartStep, SupportStep}

object TurnManagers:
  enum TurnAction:
    case CompleteMission
    case BuyFace
    case ActivateSupport
    case EndTurn

    def availableSteps: Seq[TurnStep] = this match
      case CompleteMission | BuyFace => Seq(MainActionStep, ExtraActionStep)
      case ActivateSupport => Seq(SupportStep)
      case EndTurn => TurnStep.values.filter(_ != StartStep)

  enum TurnStep:
    case StartStep
    case SupportStep
    case MainActionStep
    case PostMainActionStep
    case ExtraActionStep
    case PostExtraActionStep

    def transitions: Seq[TurnStep] = this match
      case StartStep => Seq(SupportStep, MainActionStep)
      case SupportStep => Seq(MainActionStep)
      case MainActionStep => Seq(PostMainActionStep)
      case PostMainActionStep => Seq(ExtraActionStep, StartStep)
      case ExtraActionStep => Seq(PostExtraActionStep, StartStep)
      case PostExtraActionStep => Seq(StartStep)

  case class TurnManager(currentStep: TurnStep):
    def changeStep(step: TurnStep): TurnManager =
      if isTransitionAvailable(step) then this.copy(step) else this

    def isTransitionAvailable(step: TurnStep): Boolean =
      currentStep.transitions.contains(step)
