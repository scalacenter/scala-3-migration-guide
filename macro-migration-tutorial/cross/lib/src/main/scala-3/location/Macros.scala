package location

import scala.quoted.{QuoteContext, Expr}

object Macros:
  inline def location: Location = ${locationImpl}

  private def locationImpl(using ctx: QuoteContext): Expr[Location] =
    import ctx.tasty.rootPosition
    val file = Expr(rootPosition.sourceFile.jpath.toString)
    val line = Expr(rootPosition.startLine + 1)
    '{new Location($file, $line)}
