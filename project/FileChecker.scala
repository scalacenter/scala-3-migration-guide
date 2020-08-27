import java.io.File
import scala.io.Source
import scala.collection.JavaConverters._
import sbt.util.Logger
import com.github.difflib.text._
import sbt.internal.util.MessageOnlyException

class FileChecker(logger: Logger) {
  private val diffGen = DiffRowGenerator.create()
    .showInlineDiffs(true)
    .inlineDiffByWord(true)
    .oldTag(f => "**")
    .build()

  def check(inputDir: File, checkDir: File): Unit = {
    val inputFiles = inputDir.listFiles
      .filter(_.isFile)
      .map(f => f.getName() -> f)
      .toMap

    val checkFiles = checkDir.listFiles().filter(_.isFile())

    val initialReport = Report(0, 0)

    val finalReport = checkFiles.foldLeft(initialReport) {
      (report, checkFile) =>
        val fileName = checkFile.getName
        inputFiles.get(fileName) match {
          case None => report.missingFile(fileName)
          case Some(inputFile) =>
            val diff = compare(inputFile, checkFile)
            if (diff.isEmpty) report.success(fileName)
            else report.fileDiff(fileName, diff) 
        }
    }

    if (finalReport.failed > 0)
      throw new MessageOnlyException(s"${finalReport.failed} file checks failed")
  }

  private def compare(inputFile: File, checkFile: File): Seq[String] = {
    val diff = diffGen.generateDiffRows(
      Source.fromFile(inputFile).getLines().toList.asJava,
      Source.fromFile(checkFile).getLines().toList.asJava
    )
    diff.asScala.toSeq
      .zipWithIndex
      .filter { case (delta, _) => delta.getTag() != DiffRow.Tag.EQUAL }
      .map { case (delta, line) => s"line ${line}:" + delta.getOldLine }
  }

  private case class Report(succeeded: Int, failed: Int) {
    def missingFile(name: String): Report = {
      logger.error(s"missing input file $name")
      Report(succeeded, failed + 1)
    }

    def fileDiff(name: String, diff: Seq[String]): Report = {
      logger.error(s"diff in file $name")
      diff.foreach(logger.error(_))
      Report(succeeded, failed + 1)
    }

    def success(name: String): Report = {
      logger.info(s"no diff in $name")
      Report(succeeded + 1, failed)
    }
  }
}

object FileChecker {
  def apply(logger: Logger): FileChecker = new FileChecker(logger)
}
