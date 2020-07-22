import org.apache.commons.io.FileUtils
import sbt.librarymanagement.CrossVersion
import xsbti.compile.CompileAnalysis

val scala213 = "2.13.3"
val dotty = "0.25.0-RC2"

val inputDir = settingKey[File]("Directory containing the source files that will be rewitten")
val outputDir = settingKey[File]("Directory in which the source files are rewritten")
val checkDir = settingKey[File]("Directory that contains the expected rewritten files")
val migration = settingKey[String]("Target Scala version - 3.0 or 3.1")

val CompileBackward = Configuration.of("CompileBackward", "compile-bwd")

val rewrites = (project in file("rewrites"))
  .settings(
    scalaVersion := dotty,
    migration := "3.0",
    scalacOptions ++= Seq(s"-source:${migration.value}-migration", "-rewrite"),
    inputDir := baseDirectory.value / s"src/input/scala-${migration.value}",
    outputDir := target.value / s"src-managed/main/scala-${migration.value}",
    checkDir := baseDirectory.value / s"src/check/scala-${migration.value}",
    Compile / sourceGenerators += Def.task { copySources(inputDir.value, outputDir.value) },
    Test / test := {
      val _ = (Compile / compile).value
      val fileChecker = new FileChecker(outputDir.value, checkDir.value, streams.value.log)
      fileChecker.run()
    }
  )

val incompatSettings = inConfig(CompileBackward)(Defaults.compileSettings) ++ 
  Seq(
    scalaVersion := dotty,
    crossScalaVersions := List(scala213, dotty),
    scalacOptions ++= CrossVersion.partialVersion(scalaVersion.value).toSeq.flatMap {
      case (0, _) => Seq("-source:3.0-migration", "-language:implicitConversions")
      case _ => Seq("-feature", "-language:implicitConversions")
    },
    Compile / unmanagedSourceDirectories := Seq(baseDirectory.value / s"src/main/scala"),
    CompileBackward / unmanagedSourceDirectories := Seq(baseDirectory.value / s"src/main/scala-2.13"),
    CompileBackward / managedClasspath := (managedClasspath in Compile).value,
    Test / test := {
      val _ = (Compile / compile).value
      checkIncompatibility(
        name.value,
        scalaVersion.value,
        (CompileBackward / compile).result.value,
        streams.value.log
      )
    }
  )

lazy val incompat = (project in file("incompat"))
  .configs(CompileBackward)
  .aggregate(
    typeInfer1, typeInfer2, typeInfer3, typeOfImplicitDef, anonymousTypeParam, defaultParamVariance,
    ambiguousConversion, reflectiveCall, explicitCallToUnapply
  )

lazy val typeInfer1 = (project in file("incompat/type-infer-1")).settings(incompatSettings)
lazy val typeInfer2 = (project in file("incompat/type-infer-2")).settings(incompatSettings)
lazy val typeInfer3 = (project in file("incompat/type-infer-3")).settings(incompatSettings)
lazy val typeOfImplicitDef = (project in file("incompat/type-of-implicit-def")).settings(incompatSettings)
lazy val anonymousTypeParam = (project in file ("incompat/anonymous-type-param")).settings(incompatSettings)
lazy val defaultParamVariance = (project in file("incompat/default-param-variance")).settings(incompatSettings)
lazy val earlyInitializer = (project in file("incompat/early-initializer")).settings(incompatSettings)
lazy val ambiguousConversion = (project in file("incompat/ambiguous-conversion")).settings(incompatSettings)
lazy val reflectiveCall = (project in file("incompat/reflective-call")).settings(incompatSettings)
lazy val explicitCallToUnapply = (project in file("incompat/explicit-call-to-unapply")).settings(incompatSettings)

def copySources(inputDir: File, outputDir: File): Seq[File] = {
  if (outputDir.exists) FileUtils.deleteDirectory(outputDir)
  FileUtils.copyDirectory(inputDir, outputDir)
  outputDir.listFiles.filter(_.isFile)
}

def checkIncompatibility(name: String, scalaVersion: String, compileResult: Result[CompileAnalysis], log: Logger): Unit = {
  CrossVersion.partialVersion(scalaVersion).foreach {
    case (0, _) => compileResult match {
      case Value(_) => 
        throw new MessageOnlyException(
          "Compilation has succeeded but failure was expected. " + 
          s"The '$name' incompatibility is probably fixed, in version $scalaVersion."
        )
      case Inc(_) =>
        log.info(s"$name is incompatible with $scalaVersion")
    }
    
    case (2, _) => compileResult match { 
      case Value(_) => ()
      case Inc(_) => 
        throw new MessageOnlyException(s"$name does not compile with version $scalaVersion anymore.")
    }

    case _ => ()
  }
}
