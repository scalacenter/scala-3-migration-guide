package location

import scala.language.experimental.macros
import scala.quoted.{QuoteContext, Expr}

object Macros:
  def location: Location = macro Scala2Macros.locationImpl
  inline def location: Location = ${locationImpl}

  def locationImpl(using ctx: QuoteContext): Expr[Location] =
    import ctx.reflect.rootPosition
    val file = Expr(rootPosition.sourceFile.jpath.toString)
    val line = Expr(rootPosition.startLine + 1)
    '{new Location($file, $line)}
