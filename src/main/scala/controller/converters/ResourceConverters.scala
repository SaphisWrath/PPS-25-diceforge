package controller.converters

import model.resource.*

object ResourceConverters:
  
  def resourceToString(resource: Resource): String = resource match
    case Gold(_) => "Oro"
    case SunCrystal(_) => "Cristalli Sola"
    case MoonCrystal(_) => "Cristalli Lunari"
    case GloryPoint(_) => "Punti Gloria"
    
  def stringToResourceBuilder(string: String)(amount: Int):Resource =string match
    case "Oro" => Gold(amount)
    case "Cristalli Solari" => SunCrystal(amount)
    case "Cristalli Lunari" => MoonCrystal(amount)
    case "Punti Gloria" => GloryPoint(amount)

