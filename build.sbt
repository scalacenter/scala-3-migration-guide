import org.apache.commons.io.FileUtils
import sbt.librarymanagement.CrossVersion
import xsbti.compile.CompileAnalysis

val scala213 = "2.13.3"
val dotty = "0.26.0-RC1"

val inputDir = settingKey[File]("Directory containing the source files that will be rewitten")
val outputDir = settingKey[File]("Directory in which the source files are rewritten")
val checkDir = settingKey[File]("Directory that contains the expected rewritten files")
val migration = settingKey[String]("Target Scala version - 3.0 or 3.1")

val CompileBackward = Configuration.of("CompileBackward", "compile-bwd")

lazy val website = project
  .in(file("website"))
  .enablePlugins(MdocPlugin, DocusaurusPlugin)

val rewrites = (project in file("rewrites"))
  .settings(
    scalaVersion := dotty,
    migration := "3.0",
    scalacOptions ++= { 
      migration.value match {
        case "3.1-deprecation" => Seq(s"-source:3.1", "-deprecation", "-rewrite")
        case version @ ("3.0" | "3.1") => Seq(s"-source:$version-migration", "-rewrite")
      }
    },
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


lazy val incompat = (project in file("incompat"))
  .configs(CompileBackward)
  .aggregate(
    typeInfer1, typeInfer2, typeInfer3, typeInfer4,  typeInfer5, typeInfer6, typeInfer7, typeInfer8, typeInfer9,
    typeInfer10, typeOfImplicitDef, anonymousTypeParam, defaultParamVariance, ambiguousConversion, reflectiveCall,
    explicitCallToUnapply, implicitView, any2stringaddConversion, typeParamIdentifier, restrictedOperator,
    existentialType, byNameParamTypeInfer, accessModifier, javaLangEnum, viewBound, abstractOverride,
    indentation1, indentation2
  )

// compile incompatibilities
lazy val typeInfer1 = (project in file("incompat/type-infer-1")).settings(incompatSettings)
lazy val typeInfer2 = (project in file("incompat/type-infer-2")).settings(incompatSettings)
lazy val typeInfer3 = (project in file("incompat/type-infer-3")).settings(incompatSettings)
lazy val typeInfer4 = (project in file("incompat/type-infer-4")).settings(incompatSettings)
lazy val typeInfer5 = (project in file("incompat/type-infer-5")).settings(incompatSettings)
lazy val typeInfer6 = (project in file("incompat/type-infer-6")).settings(incompatSettings)
lazy val typeInfer7 = (project in file("incompat/type-infer-7")).settings(incompatSettings)
lazy val typeInfer8 = (project in file("incompat/type-infer-8")).settings(incompatSettings)
lazy val typeInfer9 = (project in file("incompat/type-infer-9")).settings(incompatSettings)
lazy val typeInfer10 = (project in file("incompat/type-infer-10")).settings(incompatSettings)
lazy val typeOfImplicitDef = (project in file("incompat/type-of-implicit-def")).settings(incompatSettings)
lazy val anonymousTypeParam = (project in file ("incompat/anonymous-type-param")).settings(incompatSettings)
lazy val defaultParamVariance = (project in file("incompat/default-param-variance")).settings(incompatSettings)
lazy val earlyInitializer = (project in file("incompat/early-initializer")).settings(incompatSettings)
lazy val ambiguousConversion =
  (project in file("incompat/ambiguous-conversion"))
    .settings(incompatSettings)
    .settings(scalacOptions += "-language:implicitConversions")
lazy val reflectiveCall = (project in file("incompat/reflective-call")).settings(incompatSettings)
lazy val explicitCallToUnapply = (project in file("incompat/explicit-call-to-unapply")).settings(incompatSettings)
lazy val any2stringaddConversion = (project in file("incompat/any2stringadd-conversion")).settings(incompatSettings)
lazy val typeParamIdentifier = (project in file("incompat/type-param-identifier")).settings(incompatSettings)
lazy val restrictedOperator = (project in file ("incompat/restricted-operator")).settings(incompatSettings)
lazy val existentialType = (project in file ("incompat/existential-type")).settings(incompatSettings)
lazy val byNameParamTypeInfer = (project in file ("incompat/by-name-param-type-infer")).settings(incompatSettings)
lazy val accessModifier = (project in file ("incompat/access-modifier")).settings(incompatSettings)
lazy val javaLangEnum = (project in file("incompat/java-lang-enum")).settings(incompatSettings)
lazy val viewBound = (project in file("incompat/view-bound")).settings(incompatSettings)
lazy val abstractOverride = (project in file("incompat/abstract-override")).settings(incompatSettings)
lazy val indentation1 = (project in file ("incompat/indentation-1")).settings(incompatSettings)
lazy val indentation2 = (project in file ("incompat/indentation-2")).settings(incompatSettings)

// runtime incompatibilities
lazy val implicitView =
  (project in file("incompat/implicit-view"))
    .settings(runtimeIncompatSettings)
    .settings(scalacOptions += "-language:implicitConversions")

lazy val incompatSettings = inConfig(CompileBackward)(Defaults.compileSettings) ++
  Seq(
    scalaVersion := dotty,
    crossScalaVersions := List(scala213, dotty),
    Compile / unmanagedSourceDirectories := Seq(baseDirectory.value / "src/main/scala"),
    CompileBackward / unmanagedSourceDirectories := Seq(baseDirectory.value / "src/main/scala-2.13"),
    CompileBackward / managedClasspath := (managedClasspath in Compile).value,
    Test / test := {
      val _ = (Compile / compile).value
      checkIncompatibility(
        name.value,
        isDotty.value,
        scalaVersion.value,
        (CompileBackward / compile).result.value,
        streams.value.log
      )
    }
  )

lazy val runtimeIncompatSettings = incompatSettings :+ {
  Test / test := {
    val _ = (Compile / run).toTask("").value
    checkRuntimeIncompatibility(
      name.value,
      isDotty.value,
      scalaVersion.value,
      (CompileBackward / run).toTask("").result.value,
      streams.value.log
    )
  }
}

def copySources(inputDir: File, outputDir: File): Seq[File] = {
  if (outputDir.exists) FileUtils.deleteDirectory(outputDir)
  FileUtils.copyDirectory(inputDir, outputDir)
  outputDir.listFiles.filter(_.isFile)
}

def checkIncompatibility(name: String, isDotty: Boolean, scalaVersion: String, compileResult: Result[CompileAnalysis], log: Logger): Unit = {
  if (isDotty) {
    compileResult match {
      case Value(_) =>
        throw new MessageOnlyException(
          "Compilation has succeeded but failure was expected. " +
          s"The '$name' incompatibility is probably fixed, in version $scalaVersion."
        )
      case Inc(_) =>
        log.info(s"$name is incompatible with $scalaVersion")
    }
  } else {
    compileResult match {
      case Value(_) => ()
      case Inc(_) =>
        throw new MessageOnlyException(s"$name does not compile with version $scalaVersion anymore.")
    }
  }
}

def checkRuntimeIncompatibility(name: String, isDotty: Boolean, scalaVersion: String, runResult: Result[Unit], log: Logger): Unit = {
  if (isDotty) {
    runResult match {
      case Value(_) =>
        throw new MessageOnlyException(
          "Run has succeeded but failure was expected. " +
          s"The '$name' incompatibility is probably fixed, in version $scalaVersion."
        )
      case Inc(_) =>
        log.info(s"$name is incompatible with $scalaVersion")
    }
  } else {
    runResult match {
      case Value(_) => ()
      case Inc(_) =>
        throw new MessageOnlyException(s"$name does not run successfully with version $scalaVersion anymore.")
    }
  }
}
