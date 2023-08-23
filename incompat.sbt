import Versions._
import IncompatSettings._

// scalafix configuration
ThisBuild / scalaVersion := scala213
ThisBuild / scalafixDependencies ++= Seq(
  "com.sandinh" %% "scala-rewrites" % "0.1.10-sd",
  "org.scala-lang" %% "scala-rewrites" % "0.1.2"
)
ThisBuild /scalafixScalaBinaryVersion := CrossVersion.binaryScalaVersion(scalaVersion.value)

/*
  List of all incompatibilities between Scala 2.13 and Scala 3.0

  You can run:
    - `++3.3.0; <incompat> / test` to validate the incompatibility as well as the Dotty rewrite if it has one
    - `++2.13.3; <incompat> / test` to cross-compile the proposed solution and validate the scalafix rule if it has one
  where <incompat> can be a single incompatibility or `incompat3` for all incompatibilities
*/
lazy val `incompat-3` = project.in(file("incompat-3"))
  .configs(CompileBackward)
  .aggregate(dotty3MigrationRewrites: _*)
  .aggregate(typeInferIncompats: _*)
  .aggregate(otherIncompats: _*)

/*
  List of all incompatibilities between Scala 3.0 and Scala 3.1
  It only contains the already existing future-migration rewrites in Dotty

  You can run:
    - `++3.3.0; <incompat> / test` to validate the Dotty rewrite
  where <incompat> can be a single incompatibility or `incompat3Future` for all incompatibilities
*/
lazy val `incompat-3-future` = project.in(file("incompat-3-future"))
  .configs(CompileBackward)
  .aggregate(dotty3FutureMigrationRewrites: _*)

/*
  Dotty migration rewrites between 2.13 and 3.0
  `dotc -source:3.0-migration -rewrite`
*/
lazy val dotty3MigrationRewrites = Seq[ProjectReference](
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

lazy val keywords = project.in(file("incompat-3/keywords")).dotty3MigrationRewriteSettings
lazy val lambdaParams = 
  project.in(file("incompat-3/lambda-params"))
    .dotty3MigrationRewriteSettings
    .settings(scalafixRewrite := true)

lazy val symbolLiterals = project.in(file("incompat-3/symbol-literals")).dotty3MigrationRewriteSettings
lazy val indentArgument = project.in(file("incompat-3/indent-argument")).dotty3MigrationRewriteSettings
lazy val doWhile = project.in(file("incompat-3/do-while")).dotty3MigrationRewriteSettings
lazy val procedureSyntax =
  project.in(file("incompat-3/procedure-syntax"))
    .dotty3MigrationRewriteSettings
    .settings(scalafixRewrite := true)
lazy val autoApplication =
  project.in(file("incompat-3/auto-application"))
    .dotty3MigrationRewriteSettings
    .settings(scalafixRewrite := true)
lazy val inheritanceShadowing = project.in(file("incompat-3/inheritance-shadowing")).dotty3MigrationRewriteSettings
lazy val valueEtaExpansion =
  project.in(file("incompat-3/value-eta-expansion"))
    .dotty3MigrationRewriteSettings
    // does not add enclosing parens like dotc does
    // .settings(scalafixRewrite := true)

/*
  Type inference incompatibilities
*/
lazy val typeInferIncompats = Seq[ProjectReference](
  typeInfer3,
  typeInfer4,
  typeInfer5,
  typeInfer9,
  typeInfer10,
) 


lazy val typeInfer3 = project.in(file("incompat-3/type-infer-3")).incompat3Settings
lazy val typeInfer4 = project.in(file("incompat-3/type-infer-4")).incompat3Settings
lazy val typeInfer5 = project.in(file("incompat-3/type-infer-5")).incompat3Settings
lazy val typeInfer9 = project.in(file("incompat-3/type-infer-9")).incompat3Settings
lazy val typeInfer10 = project.in(file("incompat-3/type-infer-10")).incompat3Settings

/*
  Other incompatibilities
  They all are compile time incompatibilities, except `implicitView`
*/
lazy val otherIncompats = Seq[ProjectReference](
  anonymousTypeParam,
  indentation1,
  restrictedOperator,
  typeParamIdentifier,
  abstractOverride,
  any2stringaddConversion,
  byNameParamTypeInfer,
  variance,
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
  patternMatch,
  `bean-property`,
  `infer-return-type`
)

// Syntactic incompatibilities
lazy val anonymousTypeParam = project.in(file("incompat-3/anonymous-type-param")).incompat3Settings
lazy val indentation1 = project.in(file("incompat-3/indentation/indentation-1")).incompat3Settings
lazy val restrictedOperator = project.in(file("incompat-3/restricted-operator")).incompat3Settings
lazy val typeParamIdentifier = project.in(file("incompat-3/type-param-identifier")).incompat3Settings

// Changed or dropped features
lazy val abstractOverride = project.in(file("incompat-3/abstract-override")).incompat3Settings
lazy val any2stringaddConversion = project.in(file("incompat-3/any2stringadd-conversion")).incompat3Settings
lazy val byNameParamTypeInfer = project.in(file("incompat-3/by-name-param-type-infer")).incompat3Settings
lazy val variance = project.in(file("incompat-3/variance")).incompat3Settings
lazy val earlyInitializer = project.in(file("incompat-3/early-initializer")).incompat3Settings
lazy val existentialType = project.in(file("incompat-3/existential-type")).incompat3Settings
lazy val wildcardArgument = project.in(file("incompat-3/wildcard-argument")).incompat3Settings
lazy val explicitCallToUnapply = project.in(file("incompat-3/explicit-call-to-unapply")).incompat3Settings
lazy val reflectiveCall = project.in(file("incompat-3/reflective-call")).incompat3Settings
lazy val caseClassCompanion = project.in(file("incompat-3/case-class-companion")).incompat3Settings
lazy val patternMatch = project.in(file("incompat-3/pattern-match")).incompat3Settings
lazy val `bean-property` = project.in(file("incompat-3/bean-property")).incompat3Settings
lazy val `infer-return-type` = project.in(file("incompat-3/infer-return-type")).incompat3Settings

// Contextual abstraction incompatibilities
lazy val ambiguousConversion =
  project.in(file("incompat-3/ambiguous-conversion"))
    .incompat3Settings
    .settings(scalacOptions += "-language:implicitConversions")
lazy val typeOfImplicitDef = 
  project.in(file("incompat-3/type-of-implicit-def"))
    .incompat3Settings
    .settings(scalafixRewrite := true)
lazy val accessModifier = project.in(file("incompat-3/access-modifier")).incompat3Settings
lazy val viewBound = project.in(file("incompat-3/view-bound"))
  .incompat3Settings
  .settings(scalacOptions += "-deprecation")

// This one is a runtime incompatibility
// It compiles but the runtime behavior is different
// In this case Scala 3 throws an `AssertionError`
lazy val implicitView =
  project.in(file("incompat-3/implicit-view"))
    .runtimeIncompat3Settings
    .settings(scalacOptions += "-language:implicitConversions")

/*
  Dotty migration rewrites between 3.0 and 3.1
  `dotc -source:future-migration -rewrite`
*/
lazy val dotty3FutureMigrationRewrites = Seq[ProjectReference](
  alphanumericInfix,
  contextBoundArg,
  etaExpansion,
  implicitLambdaParam,
  patternBinding,
  wildcard
)

// This one is not a migration rewrite but a 3.1 deprecation rewrite
// `dotc -source:future -deprecation -rewrite`
lazy val alphanumericInfix =
  project.in(file("incompat-3-future/alphanumeric-infix"))
    .dotty3FutureMigrationRewriteSettings
    .settings(
      scalacOptions -= "-source:future-migration", 
      scalacOptions ++=  Seq("-source:future", "-deprecation") 
    )

lazy val contextBoundArg = project.in(file("incompat-3-future/context-bound-arg")).dotty3FutureMigrationRewriteSettings
lazy val etaExpansion = project.in(file("incompat-3-future/eta-expansion")).dotty3FutureMigrationRewriteSettings
lazy val implicitLambdaParam = project.in(file("incompat-3-future/implicit-lambda-param")).dotty3FutureMigrationRewriteSettings
lazy val patternBinding = project.in(file("incompat-3-future/pattern-binding")).dotty3FutureMigrationRewriteSettings
lazy val wildcard = new IncompatSettings(project.in(file("incompat-3-future/wildcard"))).dotty3FutureMigrationRewriteSettings
