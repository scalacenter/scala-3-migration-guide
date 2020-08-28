package docs

import java.nio.charset.StandardCharsets

import mdoc.{Reporter, StringModifier}

import scala.meta.inputs.{Input, Position}
import scala.meta.internal.io.FileIO
import scala.meta.io.AbsolutePath
import scala.util.Try

class FileModifier extends StringModifier {
  /*
  Usage is mdoc:file:docs/upgrade.md:skip-first-line // will skip the first line when importing the file
  mdoc:file:docs/upgrade.md // will import the entire file
   */
  override val name: String = "file"

  override def process(info: String,
                       code: Input,
                       reporter: Reporter): String = {
    val args = info.split(':').toSeq
    val file = AbsolutePath(args.headOption.getOrElse(""))
    val skipFirstLine = Try(args(1)).toOption match {
      case Some("skip-first-line") => true
      case _ => false
    }
    if (file.isFile) {
      val text = FileIO.slurp(file, StandardCharsets.UTF_8).trim
      if (skipFirstLine) text.linesIterator.drop(1).toList.mkString("\n") else text
    } else {
      val pos = Position.Range(code, 0 - info.length - 1, 0 - 1)
      reporter.error(pos, s"no such file: $file")
      ""
    }
  }
}
