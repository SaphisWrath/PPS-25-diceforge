package controller.converters

import model.turn.TurnManagers.TurnStep
import model.turn.TurnManagers.TurnStep.*
import view.LanguageStrings.TurnStepStrings.*

object TurnStepConverter:
  def toString(turnStep: TurnStep): String = turnStep match
    case StartStep => start
    case SupportStep => support
    case MainActionStep | ExtraActionStep => action
    case PostMainActionStep | PostExtraActionStep => actionDone

