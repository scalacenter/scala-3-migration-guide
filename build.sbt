import org.apache.commons.io.FileUtils

val scala213 = "2.13.3"
val dotty = "0.26.0-RC1"

val inputDir = settingKey[File]("Directory containing the source files that will be rewitten")
val outputDir = settingKey[File]("Directory in which the source files are rewritten")
val checkDir = settingKey[File]("Directory that contains the expected rewritten files")
val migration = settingKey[String]("Target Scala version - 3.0 or 3.1")

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
      FileChecker(streams.value.log).check(outputDir.value, checkDir.value)
    }
  )

val CompileBackward = Configuration.of("CompileBackward", "compile-bwd")
val scala213SourceDir = settingKey[File]("Directory containing the Scala 2.13 sources")
val dottySourceDir = settingKey[File]("Directory containing the Scala 3.0 sources")
val dottyRewrite = settingKey[Boolean]("Does this incompatibility have a Dotty rewrite?")
val rewriteDir = settingKey[File]("Directory where the sources are rewritten by either Dotty or Scalafix")

lazy val incompat30 = (project in file("incompat-3.0"))
  .configs(CompileBackward)
  .aggregate(dotty30MigrationRewrites: _*)
  .aggregate(typeInferIncompats: _*)
  .aggregate(otherIncompats: _*)

/*
  Dotty migration rewrites, 2.13 to 3.0
  `dotc -source:3.0-migration -rewrite`
*/
lazy val dotty30MigrationRewrites = Seq[ProjectReference](
  keywords,
  lambdaParams,
  symbolLiterals,
  indentArgument,
  doWhile,
  procedureSyntax,
  autoApplication,
  inheritanceShadowing,
  valueEtaExpansion
)

lazy val dotty30MigrationRewriteSettings = incompatSettings ++ Seq(dottyRewrite := true)

lazy val keywords =
  project.in(file("incompat-3.0/keywords"))
    .settings(dotty30MigrationRewriteSettings)

lazy val lambdaParams = 
  project.in(file("incompat-3.0/lambda-params"))
    .settings(dotty30MigrationRewriteSettings)

lazy val symbolLiterals =
  project.in(file("incompat-3.0/symbol-literals"))
    .settings(dotty30MigrationRewriteSettings)

lazy val indentArgument =
  project.in(file("incompat-3.0/indent-argument"))
    .settings(dotty30MigrationRewriteSettings)

lazy val doWhile =
  project.in(file("incompat-3.0/do-while"))
    .settings(dotty30MigrationRewriteSettings)

lazy val procedureSyntax =
  project.in(file("incompat-3.0/procedure-syntax"))
    .settings(dotty30MigrationRewriteSettings)

lazy val autoApplication =
  project.in(file("incompat-3.0/auto-application"))
    .settings(dotty30MigrationRewriteSettings)

lazy val inheritanceShadowing =
  project.in(file("incompat-3.0/inheritance-shadowing"))
    .settings(dotty30MigrationRewriteSettings)

lazy val valueEtaExpansion =
  project.in(file("incompat-3.0/value-eta-expansion"))
    .settings(dotty30MigrationRewriteSettings)

/*
  Type inference incompatibilities
*/
lazy val typeInferIncompats = Seq[ProjectReference](
  typeInfer1,
  typeInfer2,
  typeInfer3,
  typeInfer4,
  typeInfer5,
  typeInfer6,
  typeInfer7,
  typeInfer8,
  typeInfer9,
  typeInfer10
) 

lazy val typeInfer1 = (project in file("incompat-3.0/type-infer-1")).settings(incompatSettings)
lazy val typeInfer2 = (project in file("incompat-3.0/type-infer-2")).settings(incompatSettings)
lazy val typeInfer3 = (project in file("incompat-3.0/type-infer-3")).settings(incompatSettings)
lazy val typeInfer4 = (project in file("incompat-3.0/type-infer-4")).settings(incompatSettings)
lazy val typeInfer5 = (project in file("incompat-3.0/type-infer-5")).settings(incompatSettings)
lazy val typeInfer6 = (project in file("incompat-3.0/type-infer-6")).settings(incompatSettings)
lazy val typeInfer7 = (project in file("incompat-3.0/type-infer-7")).settings(incompatSettings)
lazy val typeInfer8 = (project in file("incompat-3.0/type-infer-8")).settings(incompatSettings)
lazy val typeInfer9 = (project in file("incompat-3.0/type-infer-9")).settings(incompatSettings)
lazy val typeInfer10 = (project in file("incompat-3.0/type-infer-10")).settings(incompatSettings)

/*
  Other incompatibilities
  All compile time incompatibilities, except `implicitView`
*/
lazy val otherIncompats = Seq[ProjectReference](
  anonymousTypeParam,
  indentation1,
  indentation2,
  restrictedOperator,
  typeParamIdentifier,
  abstractOverride,
  any2stringaddConversion,
  byNameParamTypeInfer,
  defaultParamVariance,
  earlyInitializer,
  existentialType,
  explicitCallToUnapply,
  javaLangEnum,
  reflectiveCall,
  ambiguousConversion,
  implicitView,
  typeOfImplicitDef,
  viewBound
)

// Syntactic incompatibilities
lazy val anonymousTypeParam = (project in file ("incompat-3.0/anonymous-type-param")).settings(incompatSettings)
lazy val indentation1 = (project in file ("incompat-3.0/indentation-1")).settings(incompatSettings)
lazy val indentation2 = (project in file ("incompat-3.0/indentation-2")).settings(incompatSettings)
lazy val restrictedOperator = (project in file ("incompat-3.0/restricted-operator")).settings(incompatSettings)
lazy val typeParamIdentifier = (project in file("incompat-3.0/type-param-identifier")).settings(incompatSettings)

// Changed or dropped features
lazy val abstractOverride = (project in file("incompat-3.0/abstract-override")).settings(incompatSettings)
lazy val any2stringaddConversion = (project in file("incompat-3.0/any2stringadd-conversion")).settings(incompatSettings)
lazy val byNameParamTypeInfer = (project in file ("incompat-3.0/by-name-param-type-infer")).settings(incompatSettings)
lazy val defaultParamVariance = (project in file("incompat-3.0/default-param-variance")).settings(incompatSettings)
lazy val earlyInitializer = (project in file("incompat-3.0/early-initializer")).settings(incompatSettings)
lazy val existentialType = (project in file ("incompat-3.0/existential-type")).settings(incompatSettings)
lazy val explicitCallToUnapply = (project in file("incompat-3.0/explicit-call-to-unapply")).settings(incompatSettings)
lazy val javaLangEnum = (project in file("incompat-3.0/java-lang-enum")).settings(incompatSettings)
lazy val reflectiveCall = (project in file("incompat-3.0/reflective-call")).settings(incompatSettings)

// Contextual abstraction incompatibilities
lazy val ambiguousConversion =
  (project in file("incompat-3.0/ambiguous-conversion"))
    .settings(incompatSettings)
    .settings(scalacOptions += "-language:implicitConversions")
lazy val typeOfImplicitDef = (project in file("incompat-3.0/type-of-implicit-def")).settings(incompatSettings)
lazy val accessModifier = (project in file ("incompat-3.0/access-modifier")).settings(incompatSettings)
lazy val viewBound = (project in file("incompat-3.0/view-bound")).settings(incompatSettings)

// This one is a runtime incompatibility
// It compiles but the runtime behavior is different
// In this case Dotty throws an `AssertionError`
lazy val implicitView =
  (project in file("incompat-3.0/implicit-view"))
    .settings(runtimeIncompatSettings)
    .settings(scalacOptions += "-language:implicitConversions")


lazy val incompatSettings = 
  inConfig(CompileBackward)(Defaults.compileSettings) ++
  Seq(
    scalaVersion := dotty,
    crossScalaVersions := List(scala213, dotty),
    
    scala213SourceDir := baseDirectory.value / s"src/main/scala-2.13",
    dottySourceDir := baseDirectory.value / s"src/main/scala",  
    dottyRewrite := false,
    rewriteDir := target.value / s"src-managed/main/scala",

    Compile / unmanagedSourceDirectories := Seq(dottySourceDir.value),
    // we copy the scala213 sources into the target folder
    // because it might be rewritten by dotc or scalafix
    CompileBackward / sourceGenerators += 
      Def.task { copySources(scala213SourceDir.value, rewriteDir.value) },
    CompileBackward / managedClasspath := (managedClasspath in Compile).value,
    CompileBackward / scalacOptions ++= {
      if (isDotty.value && dottyRewrite.value)
        Seq(s"-source:3.0-migration", "-rewrite")
      else Seq.empty
    },
    Test / test := {
      val logger = streams.value.log
      val _ = (Compile / compile).value
      val compileBwd = (CompileBackward / compile).result.value
      if (isDotty.value && dottyRewrite.value)
        Assert.dottyRewrite(
          name.value,
          scalaVersion.value,
          compileBwd,
          rewriteDir.value,
          dottySourceDir.value,
          logger
        )
      else if (isDotty.value && !dottyRewrite.value)
        Assert.dottyIncompatibility(name.value, scalaVersion.value, compileBwd, logger)
      else
        Assert.scala2Compilation(name.value, scalaVersion.value, compileBwd, logger)
    }
  )

lazy val runtimeIncompatSettings = incompatSettings :+ {
  Test / test := {
    val logger = streams.value.log
    val _ = (Compile / run).toTask("").value
    val runBwd = (CompileBackward / run).toTask("").result.value
    
    if (isDotty.value)
      Assert.dottyRuntimeIncompatibility(name.value, scalaVersion.value, runBwd, logger)
    else
      Assert.scala2Run(name.value, scalaVersion.value, runBwd, logger)
  }
}

def copySources(inputDir: File, outputDir: File): Seq[File] = {
  if (outputDir.exists) FileUtils.deleteDirectory(outputDir)
  FileUtils.copyDirectory(inputDir, outputDir)
  outputDir.listFiles.filter(_.isFile)
}
