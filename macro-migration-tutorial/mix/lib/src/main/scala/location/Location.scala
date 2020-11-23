package location

import scala.reflect.macros.blackbox.Context

case class Location(path: String, line: Int) {
  override def toString(): String = s"Line $line in $path"
}

object Scala2Macros {
  def locationImpl(c: Context): c.Tree =  {
    import c.universe._
    val location = typeOf[Location]
    val line = Literal(Constant(c.enclosingPosition.line))
    val path = Literal(Constant(c.enclosingPosition.source.path))
    q"new $location($path, $line)"
  }
}
