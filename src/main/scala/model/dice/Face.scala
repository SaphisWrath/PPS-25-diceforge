package model.dice

import model.dice.Effect
import model.resource.ResourceBoard

enum Face:
  case SumFace(effects: List[Effect[ResourceBoard]])
  case OptionFace(effects: List[Effect[ResourceBoard]])

