package location

import scala.quoted.{Quotes, Expr}

object Macros:
  inline def location: Location = ${locationImpl}

  private def locationImpl(using ctx: Quotes): Expr[Location] =
    import ctx.reflect.Position
    val pos = Position.ofMacroExpansion
    val file = Expr(pos.sourceFile.jpath.toString)
    val line = Expr(pos.startLine + 1)
    '{new Location($file, $line)}
