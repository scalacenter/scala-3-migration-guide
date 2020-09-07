import sbt._
import xsbti.compile.CompileAnalysis

object Assert {
  def dottyRewrite(
    name: String,
    scalaVersion: String,
    compileResult: Result[CompileAnalysis],
    rewriteDir: File,
    dottySourceDir: File,
    logger: Logger
  ): Unit = {
    compileResult match {
      case Inc(_) =>
        throw new MessageOnlyException(s"$name does not compile with Dotty version $scalaVersion in migration mode.")
      case Value(_) =>
        FileChecker(logger).check(rewriteDir, dottySourceDir)
        logger.info(s"$name is rewritten successfully by Dotty")
    }
  }

  def scalafixRewrite(
    name: String,
    scalaVersion: String,
    compileResult: Result[CompileAnalysis],
    rewriteDir: File,
    dottySourceDir: File,
    logger: Logger
  ): Unit = {
    compileResult match {
      case Inc(_) =>
        throw new MessageOnlyException(s"$name does not compile with Scala version $scalaVersion.")
      case Value(_) =>
        FileChecker(logger).check(rewriteDir, dottySourceDir)
        logger.info(s"$name is rewritten successfully by Scalafix")
    }
  }

  def incompatibility(
    name: String,
    scalaVersion: String,
    compileResult: Result[CompileAnalysis],
    logger: Logger
  ): Unit = {
    compileResult match {
      case Value(_) =>
        throw new MessageOnlyException(
          "Compilation has succeeded but failure was expected. " +
          s"The '$name' incompatibility is probably fixed, in version $scalaVersion."
        )
      case Inc(_) =>
        logger.info(s"$name is incompatible with $scalaVersion")
    }
  }

  def compilation(
    name: String,
    scalaVersion: String,
    compileResult: Result[CompileAnalysis],
    logger: Logger
  ): Unit = {
    compileResult match {
      case Value(_) => ()
      case Inc(_) =>
        throw new MessageOnlyException(s"$name does not compile with version $scalaVersion anymore.")
    }
  }

  def runtimeIncompatibility(
    name: String,
    scalaVersion: String,
    runResult: Result[Unit],
    logger: Logger
  ): Unit = {
    runResult match {
      case Value(_) =>
        throw new MessageOnlyException(
          "Run has succeeded but failure was expected. " +
          s"The '$name' incompatibility is probably fixed, in version $scalaVersion."
        )
      case Inc(_) =>
        logger.info(s"$name is runtime incompatible with $scalaVersion")
    }
  }

  def run(
    name: String,
    scalaVersion:String,
    runResult: Result[Unit],
    logger: Logger
  ): Unit = {
    runResult match {
      case Value(_) => ()
      case Inc(_) =>
        throw new MessageOnlyException(s"$name does not run successfully with version $scalaVersion anymore.")
    }
  }
}
