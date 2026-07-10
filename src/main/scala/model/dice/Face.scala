package model.dice

import model.effects.Effect

enum Face:
  case SumFace(effects: List[Effect])
  case OptionFace(effects: List[Effect])

