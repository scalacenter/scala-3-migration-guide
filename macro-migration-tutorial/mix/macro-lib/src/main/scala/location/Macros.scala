package location

import scala.language.experimental.macros
import scala.quoted.{Quotes, Expr}

object Macros:
  def location: Location = macro Scala2Macros.locationImpl
  inline def location: Location = ${locationImpl}

  def locationImpl(using quotes: Quotes): Expr[Location] =
    import quotes.reflect.Position
    val file = Expr(Position.ofMacroExpansion.sourceFile.jpath.toString)
    val line = Expr(Position.ofMacroExpansion.startLine + 1)
    '{new Location($file, $line)}
