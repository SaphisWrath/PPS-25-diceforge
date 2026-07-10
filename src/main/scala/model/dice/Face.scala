package model.dice

import model.dice.Effect
import model.resource.Resource

enum Face:
  case SumFace(effects: List[Effect[Resource]])
  case OptionFace(effects: List[Effect[Resource]])

