import sbt._
import sbt.Keys._
import scalafix.sbt.ScalafixPlugin.autoImport._
import Versions._

object IncompatSettings {
  val CompileBackward = Configuration.of("CompileBackward", "compile-bwd")
  val scala213SourceDir = settingKey[File]("Directory containing the Scala 2.13 sources")
  val scala3SourceDir = settingKey[File]("Directory containing the Scala 3 sources")
  val scala3FutureSourceDir = settingKey[File]("Directory containing the Scala 3.1 sources")
  val dottyRewrite = settingKey[Boolean]("Does this incompatibility have a Dotty rewrite?")
  val scalafixRewrite = settingKey[Boolean]("Does this incompatibility have a Scalafix rewrite?")
  val rewriteDir = settingKey[File]("Directory where the sources are rewritten by either Dotty or Scalafix")
  val isScala3 = Def.settingKey[Boolean]("")

  implicit def apply(project: Project): IncompatSettings = new IncompatSettings(project)
}

class IncompatSettings(project: Project) {
  import IncompatSettings._

  def dotty3MigrationRewriteSettings = project.incompat3Settings.settings(dottyRewrite := true)

  def dotty3FutureMigrationRewriteSettings = project.incompat3FutureSettings.settings(dottyRewrite := true)

  def incompat3Settings: Project = project
    .configs(CompileBackward)
    .settings(
      inConfig(CompileBackward)(Defaults.configSettings),
      inConfig(CompileBackward)(scalafixConfigSettings(CompileBackward)),
      isScala3 := scalaVersion.value.startsWith("3"),
      scalaVersion := scala3,
      crossScalaVersions := List(scala213, scala3),
      
      scala213SourceDir := baseDirectory.value / s"src/main/scala-2.13",
      scala3SourceDir := baseDirectory.value / s"src/main/scala",  
      dottyRewrite := false,
      scalafixRewrite := false,
      rewriteDir := target.value / s"src-managed/main/scala",

      Compile / unmanagedSourceDirectories := Seq(scala3SourceDir.value),
      // we copy the scala213 sources into the target folder
      // because it might be rewritten by dotc or scalafix
      CompileBackward / sourceGenerators += Def.task {
        val _ = (CompileBackward / clean).value // clean to force recompilation and rewrite
        copySources(scala213SourceDir.value, rewriteDir.value)
      },
      CompileBackward / managedClasspath := (Compile / managedClasspath).value,
      
      CompileBackward / scalacOptions ++= {
        if (isScala3.value && dottyRewrite.value)
          Seq("-source:3.0-migration", "-rewrite")
        else Seq()
      },

      // scalafix configuration
      semanticdbVersion := "4.8.8",
      semanticdbEnabled := !isScala3.value && scalafixRewrite.value,
      CompileBackward / scalafixConfig := Some(baseDirectory.value / ".scalafix.conf"),
      CompileBackward / scalafixOnCompile := !isScala3.value && scalafixRewrite.value,
      CompileBackward / scalafix / unmanagedSources := (CompileBackward / managedSources).value, // hack to force Scalafix on managedSources
      CompileBackward / scalafixCaching := false,

      Test / test := {
        val logger = streams.value.log
        val _ = (Compile / compile).value
        val compileBwd = (CompileBackward / compile).result.value
        if (isScala3.value && dottyRewrite.value)
          Assert.dottyRewrite(
            name.value,
            scalaVersion.value,
            compileBwd,
            rewriteDir.value,
            scala3SourceDir.value,
            logger
          )
        else if (isScala3.value && !dottyRewrite.value)
          Assert.incompatibility(name.value, scalaVersion.value, compileBwd, logger)
        else if (scalafixRewrite.value)
          Assert.scalafixRewrite(
            name.value,
            scalaVersion.value,
            compileBwd,
            rewriteDir.value,
            scala3SourceDir.value,
            logger
          )
        else
          Assert.compilation(name.value, scalaVersion.value, compileBwd, logger)
      }
    )

  def runtimeIncompat3Settings = project.incompat3Settings
    .settings(
      Test / test := {
        val logger = streams.value.log
        val _ = (Compile / run).toTask("").value
        val runBwd = (CompileBackward / run).toTask("").result.value

        if (isScala3.value)
          Assert.runtimeIncompatibility(name.value, scalaVersion.value, runBwd, logger)
        else
          Assert.run(name.value, scalaVersion.value, runBwd, logger)
      }
    )

  def incompat3FutureSettings = project
    .configs(CompileBackward)
    .settings(
      inConfig(CompileBackward)(Defaults.compileSettings),
      scalaVersion := scala3,
      scala3SourceDir := baseDirectory.value / s"src/main/scala-3.0",
      scala3FutureSourceDir := baseDirectory.value / s"src/main/scala",  
      dottyRewrite := false,
      rewriteDir := target.value / s"src-managed/main/scala",

      Compile / unmanagedSourceDirectories := Seq(scala3FutureSourceDir.value),
      // we copy the scala3 sources into the target folder
      // because they might be rewritten by dotc or scalafix
      CompileBackward / sourceGenerators += Def.task {
        val _ = (CompileBackward / clean).value // clean to force recompilation and rewrite
        copySources(scala3SourceDir.value, rewriteDir.value)
      },
      CompileBackward / managedClasspath := (Compile / managedClasspath).value,
      CompileBackward / scalacOptions ++= {
        if (dottyRewrite.value) Seq(s"-source:future-migration", "-rewrite")
        else Seq.empty
      },
      Test / test := {
        val logger = streams.value.log
        val _ = (Compile / compile).value
        val compileBwd = (CompileBackward / compile).result.value
        if (dottyRewrite.value)
          Assert.dottyRewrite(
            name.value,
            scalaVersion.value,
            compileBwd,
            rewriteDir.value,
            scala3FutureSourceDir.value,
            logger
          )
        else
          Assert.compilation(name.value, scalaVersion.value, compileBwd, logger)
      }
    )

  private def copySources(inputDir: File, outputDir: File): Seq[File] = {
    if (outputDir.exists) IO.delete(outputDir)
    IO.copyDirectory(inputDir, outputDir)
    outputDir.listFiles.filter(_.isFile)
  }
}
