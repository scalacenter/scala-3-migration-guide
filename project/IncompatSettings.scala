import sbt._
import sbt.Keys._
import scalafix.sbt.ScalafixPlugin.autoImport._
import Versions._

object IncompatSettings {
  val CompileBackward = Configuration.of("CompileBackward", "compile-bwd")
  val scala213SourceDir = settingKey[File]("Directory containing the Scala 2.13 sources")
  val scala30SourceDir = settingKey[File]("Directory containing the Scala 3.0 sources")
  val scala31SourceDir = settingKey[File]("Directory containing the Scala 3.1 sources")
  val dottyRewrite = settingKey[Boolean]("Does this incompatibility have a Dotty rewrite?")
  val scalafixRewrite = settingKey[Boolean]("Does this incompatibility have a Scalafix rewrite?")
  val rewriteDir = settingKey[File]("Directory where the sources are rewritten by either Dotty or Scalafix")
  val isScala3 = Def.settingKey[Boolean]("")

  implicit def apply(project: Project): IncompatSettings = new IncompatSettings(project)
}

class IncompatSettings(project: Project) {
  import IncompatSettings._

  def dotty30MigrationRewriteSettings = project.incompat30Settings.settings(dottyRewrite := true)

  def dotty31MigrationRewriteSettings = project.incompat31Settings.settings(dottyRewrite := true)

  def incompat30Settings: Project = project
    .configs(CompileBackward)
    .settings(
      inConfig(CompileBackward)(Defaults.configSettings),
      inConfig(CompileBackward)(scalafixConfigSettings(CompileBackward)),
      isScala3 := scalaVersion.value.startsWith("3"),
      scalaVersion := scala30,
      crossScalaVersions := List(scala213, scala30),
      
      scala213SourceDir := baseDirectory.value / s"src/main/scala-2.13",
      scala30SourceDir := baseDirectory.value / s"src/main/scala",  
      dottyRewrite := false,
      scalafixRewrite := false,
      rewriteDir := target.value / s"src-managed/main/scala",

      Compile / unmanagedSourceDirectories := Seq(scala30SourceDir.value),
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
      semanticdbVersion := "4.4.18",
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
            scala30SourceDir.value,
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
            scala30SourceDir.value,
            logger
          )
        else
          Assert.compilation(name.value, scalaVersion.value, compileBwd, logger)
      }
    )

  def runtimeIncompat30Settings = project.incompat30Settings
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

  def incompat31Settings = project
    .configs(CompileBackward)
    .settings(
      inConfig(CompileBackward)(Defaults.compileSettings),
      scalaVersion := scala30,
      scala30SourceDir := baseDirectory.value / s"src/main/scala-3.0",
      scala31SourceDir := baseDirectory.value / s"src/main/scala",  
      dottyRewrite := false,
      rewriteDir := target.value / s"src-managed/main/scala",

      Compile / unmanagedSourceDirectories := Seq(scala31SourceDir.value),
      // we copy the scala30 sources into the target folder
      // because they might be rewritten by dotc or scalafix
      CompileBackward / sourceGenerators += Def.task {
        val _ = (CompileBackward / clean).value // clean to force recompilation and rewrite
        copySources(scala30SourceDir.value, rewriteDir.value)
      },
      CompileBackward / managedClasspath := (Compile / managedClasspath).value,
      CompileBackward / scalacOptions ++= {
        if (dottyRewrite.value)
          Seq(s"-source:future-migration", "-rewrite")
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
            scala31SourceDir.value,
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
