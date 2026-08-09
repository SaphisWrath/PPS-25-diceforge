package model.utils.parsers
import org.virtuslab.yaml.*

object Props:
  case class ParameterProp(parType: String, value: String, parameters: Option[List[Map[String, Option[String]]]]) derives YamlCodec

  def fromMap(map: Map[String, Option[String]]): Either[String, ParameterProp] = {
    println(map.asYaml)
    map.asYaml.as[ParameterProp] match
      case Left(err) => Left(err.msg)
      case Right(v) => Right(v)
  }

  private enum Types(val name: String):
    case Class extends Types("class")
    case Int extends Types("int")

  def getParameter(prop: ParameterProp): Either[String, Any] =
    var parameterArray: Array[Any] = Array.empty
    try
      prop.parType match
        case s if s == Types.Class.name =>
          val constructor = Class.forName(prop.value).getConstructors()(0)
          if prop.parameters.isDefined then
            val parsedParameters = prop.parameters.get.map(p =>
              val prop = fromMap(p)
              prop match
                case Right(v) => getParameter(v) match
                  case Right(v) => Right(v)
                  case Left(m) => Left(m)
                case Left(m) => Left(m)
            )
            if parsedParameters.exists { case Left(_) => true }
            then Left(s"Error parsing inner parameters in ${prop.value}")
            else
              val parameterArray = parsedParameters.flatMap{case Right(content) => List(content)}.toArray
              Right(constructor.newInstance(parameterArray.asInstanceOf[Array[AnyRef]]))
          else 
            Right(constructor.newInstance())
        case s if s == Types.Int.name =>
          Right(prop.value.toInt)
    catch
      case _: ClassNotFoundException => Left(s"Class $prop.value not found")