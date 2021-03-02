import org.apache.commons.io.FileUtils
import Versions._

val CompileBackward = Configuration.of("CompileBackward", "compile-bwd")
val scala213SourceDir = settingKey[File]("Directory containing the Scala 2.13 sources")
val scala30SourceDir = settingKey[File]("Directory containing the Scala 3.0 sources")
val scala31SourceDir = settingKey[File]("Directory containing the Scala 3.1 sources")
val dottyRewrite = settingKey[Boolean]("Does this incompatibility have a Dotty rewrite?")
val scalafixRewrite = settingKey[Boolean]("Does this incompatibility have a Scalafix rewrite?")
val rewriteDir = settingKey[File]("Directory where the sources are rewritten by either Dotty or Scalafix")

// scalafix configuration
Global / excludeLintKeys += CompileBackward / scalafixConfig
inThisBuild(
  Seq(
    scalaVersion := scala213,
    scalafixDependencies ++= Seq(
      "com.sandinh" %% "scala-rewrites" % "0.1.10-sd",
      "org.scala-lang" %% "scala-rewrites" % "0.1.2"
    ),
    scalafixScalaBinaryVersion := CrossVersion.binaryScalaVersion(scalaVersion.value)
  )
)

/*
  List of all incompatibilities between Scala 2.13 and Scala 3.0

  You can run:
    - `++0.26.0; <incompat> / test` to validate the incompatibility as well as the Dotty rewrite if it has one
    - `++2.13.3; <incompat> / test` to cross-compile the proposed solution and validate the scalafix rule if it has one
  where <incompat> can be a single incompatibility or `incompat30` for all incompatibilities
*/
lazy val `incompat-30` = project.in(file("incompat-30"))
  .configs(CompileBackward)
  .aggregate(dotty30MigrationRewrites: _*)
  .aggregate(typeInferIncompats: _*)
  .aggregate(otherIncompats: _*)

/*
  List of all incompatibilities between Scala 3.0 and Scala 3.1
  It only contains the already existing 3.1-migration rewrites in Dotty

  You can run:
    - `++0.26.0; <incompat> / test` to validate the Dotty rewrite
  where <incompat> can be a single incompatibility or `incompat31` for all incompatibilities
*/
lazy val `incompat-31` = project.in(file("incompat-31"))
  .configs(CompileBackward)
  .aggregate(dotty31MigrationRewrites: _*)

/*
  Dotty migration rewrites between 2.13 and 3.0
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

lazy val dotty30MigrationRewriteSettings = incompat30Settings ++ Seq(dottyRewrite := true)

lazy val keywords =
  project.in(file("incompat-30/keywords"))
    .settings(dotty30MigrationRewriteSettings)

lazy val lambdaParams = 
  project.in(file("incompat-30/lambda-params"))
    .settings(dotty30MigrationRewriteSettings)
    .settings(scalafixRewrite := true)

lazy val symbolLiterals =
  project.in(file("incompat-30/symbol-literals"))
    .settings(dotty30MigrationRewriteSettings)

lazy val indentArgument =
  project.in(file("incompat-30/indent-argument"))
    .settings(dotty30MigrationRewriteSettings)

lazy val doWhile =
  project.in(file("incompat-30/do-while"))
    .settings(dotty30MigrationRewriteSettings)

lazy val procedureSyntax =
  project.in(file("incompat-30/procedure-syntax"))
    .settings(dotty30MigrationRewriteSettings)
    .settings(scalafixRewrite := true)

lazy val autoApplication =
  project.in(file("incompat-30/auto-application"))
    .settings(dotty30MigrationRewriteSettings)
    .settings(scalafixRewrite := true)

lazy val inheritanceShadowing =
  project.in(file("incompat-30/inheritance-shadowing"))
    .settings(dotty30MigrationRewriteSettings)

lazy val valueEtaExpansion =
  project.in(file("incompat-30/value-eta-expansion"))
    .settings(dotty30MigrationRewriteSettings)
    // does not add enclosing parens like dotc does
    // .settings(scalafixRewrite := true)

/*
  Type inference incompatibilities
*/
lazy val typeInferIncompats = Seq[ProjectReference](
  typeInfer1,
  typeInfer3,
  typeInfer4,
  typeInfer5,
  typeInfer7,
  typeInfer9,
  typeInfer10
) 

lazy val typeInfer1 = project.in(file("incompat-30/type-infer-1")).settings(incompat30Settings)
lazy val typeInfer3 = project.in(file("incompat-30/type-infer-3")).settings(incompat30Settings)
lazy val typeInfer4 = project.in(file("incompat-30/type-infer-4")).settings(incompat30Settings)
lazy val typeInfer5 = project.in(file("incompat-30/type-infer-5")).settings(incompat30Settings)
lazy val typeInfer7 = project.in(file("incompat-30/type-infer-7")).settings(incompat30Settings)
lazy val typeInfer9 = project.in(file("incompat-30/type-infer-9")).settings(incompat30Settings)
lazy val typeInfer10 = project.in(file("incompat-30/type-infer-10")).settings(incompat30Settings)

/*
  Other incompatibilities
  They all are compile time incompatibilities, except `implicitView`
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
  wildcardArgument,
  explicitCallToUnapply,
  reflectiveCall,
  ambiguousConversion,
  implicitView,
  typeOfImplicitDef,
  viewBound,
  caseClassCompanion,
  patternMatch
)

// Syntactic incompatibilities
lazy val anonymousTypeParam = project.in(file("incompat-30/anonymous-type-param")).settings(incompat30Settings)
lazy val indentation1 = project.in(file("incompat-30/indentation/indentation-1")).settings(incompat30Settings)
lazy val indentation2 = project.in(file("incompat-30/indentation/indentation-2")).settings(incompat30Settings)
lazy val restrictedOperator = project.in(file("incompat-30/restricted-operator")).settings(incompat30Settings)
lazy val typeParamIdentifier = project.in(file("incompat-30/type-param-identifier")).settings(incompat30Settings)

// Changed or dropped features
lazy val abstractOverride = project.in(file("incompat-30/abstract-override")).settings(incompat30Settings)
lazy val any2stringaddConversion = project.in(file("incompat-30/any2stringadd-conversion")).settings(incompat30Settings)
lazy val byNameParamTypeInfer = project.in(file("incompat-30/by-name-param-type-infer")).settings(incompat30Settings)
lazy val defaultParamVariance = project.in(file("incompat-30/default-param-variance")).settings(incompat30Settings)
lazy val earlyInitializer = project.in(file("incompat-30/early-initializer")).settings(incompat30Settings)
lazy val existentialType = project.in(file("incompat-30/existential-type")).settings(incompat30Settings)
lazy val wildcardArgument = project.in(file("incompat-30/wildcard-argument")).settings(incompat30Settings)
lazy val explicitCallToUnapply = project.in(file("incompat-30/explicit-call-to-unapply")).settings(incompat30Settings)
lazy val reflectiveCall = project.in(file("incompat-30/reflective-call")).settings(incompat30Settings)
lazy val caseClassCompanion = project.in(file("incompat-30/case-class-companion")).settings(incompat30Settings)
lazy val patternMatch = project.in(file("incompat-30/pattern-match")).settings(incompat30Settings)

// Contextual abstraction incompatibilities
lazy val ambiguousConversion =
  project.in(file("incompat-30/ambiguous-conversion"))
    .settings(incompat30Settings)
    .settings(scalacOptions += "-language:implicitConversions")

lazy val typeOfImplicitDef = 
  project.in(file("incompat-30/type-of-implicit-def"))
    .settings(incompat30Settings)
    .settings(scalafixRewrite := true)

lazy val accessModifier = project.in(file("incompat-30/access-modifier")).settings(incompat30Settings)
lazy val viewBound = project.in(file("incompat-30/view-bound")).settings(scalacOptions += "-deprecation").settings(incompat30Settings)

// This one is a runtime incompatibility
// It compiles but the runtime behavior is different
// In this case Dotty throws an `AssertionError`
lazy val implicitView =
  project.in(file("incompat-30/implicit-view"))
    .settings(runtimeIncompat30Settings)
    .settings(scalacOptions += "-language:implicitConversions")


/*
  Dotty migration rewrites between 3.0 and 3.1
  `dotc -source:3.1-migration -rewrite`
*/
lazy val dotty31MigrationRewrites = Seq[ProjectReference](
  alphanumericInfix,
  contextBoundArg,
  etaExpansion,
  implicitLambdaParam,
  patternBinding,
  wildcard
)

lazy val dotty31MigrationRewriteSettings = incompat31Settings ++ Seq(dottyRewrite := true)

// This one is not a migration rewrite but a 3.1 deprecation rewrite
// `dotc -source:3.1 -deprecation -rewrite`
lazy val alphanumericInfix =
  project.in(file("incompat-31/alphanumeric-infix"))
    .settings(dotty31MigrationRewriteSettings)
    .settings(
      scalacOptions -= "-source:3.1-migration", 
      scalacOptions ++=  Seq("-source:3.1", "-deprecation") 
    )

lazy val contextBoundArg =
  project.in(file("incompat-31/context-bound-arg"))
    .settings(dotty31MigrationRewriteSettings)

lazy val etaExpansion =
  project.in(file("incompat-31/eta-expansion"))
    .settings(dotty31MigrationRewriteSettings)

lazy val implicitLambdaParam =
  project.in(file("incompat-31/implicit-lambda-param"))
    .settings(dotty31MigrationRewriteSettings)

lazy val patternBinding =
  project.in(file("incompat-31/pattern-binding"))
    .settings(dotty31MigrationRewriteSettings)

lazy val wildcard =
  project.in(file("incompat-31/wildcard"))
    .settings(dotty31MigrationRewriteSettings)

lazy val incompat30Settings = 
  inConfig(CompileBackward)(Defaults.compileSettings) ++
  inConfig(CompileBackward)(scalafixConfigSettings(CompileBackward)) ++
  Seq(
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
    CompileBackward / managedClasspath := (managedClasspath in Compile).value,
    
    CompileBackward / scalacOptions ++= {
      if (isDotty.value && dottyRewrite.value)
        Seq("-source:3.0-migration", "-rewrite")
      else Seq()
    },

    // scalafix configuration
    semanticdbVersion := "4.4.10",
    semanticdbEnabled := !isDotty.value && scalafixRewrite.value,
    CompileBackward / scalafixConfig := Some(baseDirectory.value / ".scalafix.conf"),
    CompileBackward / scalafixOnCompile := !isDotty.value && scalafixRewrite.value,
    CompileBackward / scalafix / unmanagedSources := (CompileBackward / managedSources).value, // hack to force Scalafix on managedSources
    CompileBackward / scalafixCaching := false,

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
          scala30SourceDir.value,
          logger
        )
      else if (isDotty.value && !dottyRewrite.value)
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

lazy val runtimeIncompat30Settings = incompat30Settings :+ {
  Test / test := {
    val logger = streams.value.log
    val _ = (Compile / run).toTask("").value
    val runBwd = (CompileBackward / run).toTask("").result.value

    if (isDotty.value)
      Assert.runtimeIncompatibility(name.value, scalaVersion.value, runBwd, logger)
    else
      Assert.run(name.value, scalaVersion.value, runBwd, logger)
  }
}

lazy val incompat31Settings = inConfig(CompileBackward)(Defaults.compileSettings) ++
  Seq(
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
    CompileBackward / managedClasspath := (managedClasspath in Compile).value,
    CompileBackward / scalacOptions ++= {
      if (dottyRewrite.value)
        Seq(s"-source:3.1-migration", "-rewrite")
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

def copySources(inputDir: File, outputDir: File): Seq[File] = {
  if (outputDir.exists) FileUtils.deleteDirectory(outputDir)
  FileUtils.copyDirectory(inputDir, outputDir)
  outputDir.listFiles.filter(_.isFile)
}
