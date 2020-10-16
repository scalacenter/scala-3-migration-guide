package location

import scala.reflect.macros.blackbox.Context
import scala.language.experimental.macros

object Macros {
  def location: Location = macro locationImpl

  def locationImpl(c: Context): c.Tree =  {
    import c.universe._
    val location = typeOf[Location]
    val line = Literal(Constant(c.enclosingPosition.line))
    val path = Literal(Constant(c.enclosingPosition.source.path))
    q"new $location($path, $line)"
  }
}
