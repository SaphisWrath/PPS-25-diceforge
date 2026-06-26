package model.dice

enum Effect:
  case ResourceEffect(amount: Int)
  case MultiplierEffect(multiplier: Int)
  case CopyEffect()