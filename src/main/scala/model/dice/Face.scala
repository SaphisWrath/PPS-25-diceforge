package model.dice

import model.dice.Effect

enum Face:
  case SumFace(effects: List[Effect])
  case OptionFace(effects: List[Effect])

