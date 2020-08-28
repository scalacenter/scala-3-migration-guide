package docs

import java.nio.file.Paths

object Main {
  def main(args: Array[String]): Unit = {
    val out = Paths.get("website", "target", "docs")
    val base = mdoc.MainSettings()
    val settings = base
      .withOut(out)
      .withCleanTarget(false)
      .withReportRelativePaths(true)
      .withArgs(args.toList)
      .withStringModifiers(
        List(new FileModifier)
      )
    val exit = mdoc.Main.process(settings)
    if (exit != 0) sys.exit(exit)
  }
}
