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
  It only contains the already existing future-migration rewrites in Dotty

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

lazy val keywords = project.in(file("incompat-30/keywords")).dotty30MigrationRewriteSettings
lazy val lambdaParams = 
  project.in(file("incompat-30/lambda-params"))
    .dotty30MigrationRewriteSettings
    .settings(scalafixRewrite := true)

lazy val symbolLiterals = project.in(file("incompat-30/symbol-literals")).dotty30MigrationRewriteSettings
lazy val indentArgument = project.in(file("incompat-30/indent-argument")).dotty30MigrationRewriteSettings
lazy val doWhile = project.in(file("incompat-30/do-while")).dotty30MigrationRewriteSettings
lazy val procedureSyntax =
  project.in(file("incompat-30/procedure-syntax"))
    .dotty30MigrationRewriteSettings
    .settings(scalafixRewrite := true)
lazy val autoApplication =
  project.in(file("incompat-30/auto-application"))
    .dotty30MigrationRewriteSettings
    .settings(scalafixRewrite := true)
lazy val inheritanceShadowing = project.in(file("incompat-30/inheritance-shadowing")).dotty30MigrationRewriteSettings
lazy val valueEtaExpansion =
  project.in(file("incompat-30/value-eta-expansion"))
    .dotty30MigrationRewriteSettings
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

lazy val typeInfer1 = project.in(file("incompat-30/type-infer-1")).incompat30Settings
lazy val typeInfer3 = project.in(file("incompat-30/type-infer-3")).incompat30Settings
lazy val typeInfer4 = project.in(file("incompat-30/type-infer-4")).incompat30Settings
lazy val typeInfer5 = project.in(file("incompat-30/type-infer-5")).incompat30Settings
lazy val typeInfer7 = project.in(file("incompat-30/type-infer-7")).incompat30Settings
lazy val typeInfer9 = project.in(file("incompat-30/type-infer-9")).incompat30Settings
lazy val typeInfer10 = project.in(file("incompat-30/type-infer-10")).incompat30Settings

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
lazy val anonymousTypeParam = project.in(file("incompat-30/anonymous-type-param")).incompat30Settings
lazy val indentation1 = project.in(file("incompat-30/indentation/indentation-1")).incompat30Settings
lazy val restrictedOperator = project.in(file("incompat-30/restricted-operator")).incompat30Settings
lazy val typeParamIdentifier = project.in(file("incompat-30/type-param-identifier")).incompat30Settings

// Changed or dropped features
lazy val abstractOverride = project.in(file("incompat-30/abstract-override")).incompat30Settings
lazy val any2stringaddConversion = project.in(file("incompat-30/any2stringadd-conversion")).incompat30Settings
lazy val byNameParamTypeInfer = project.in(file("incompat-30/by-name-param-type-infer")).incompat30Settings
lazy val variance = project.in(file("incompat-30/variance")).incompat30Settings
lazy val earlyInitializer = project.in(file("incompat-30/early-initializer")).incompat30Settings
lazy val existentialType = project.in(file("incompat-30/existential-type")).incompat30Settings
lazy val wildcardArgument = project.in(file("incompat-30/wildcard-argument")).incompat30Settings
lazy val explicitCallToUnapply = project.in(file("incompat-30/explicit-call-to-unapply")).incompat30Settings
lazy val reflectiveCall = project.in(file("incompat-30/reflective-call")).incompat30Settings
lazy val caseClassCompanion = project.in(file("incompat-30/case-class-companion")).incompat30Settings
lazy val patternMatch = project.in(file("incompat-30/pattern-match")).incompat30Settings
lazy val `bean-property` = project.in(file("incompat-30/bean-property")).incompat30Settings
lazy val `infer-return-type` = project.in(file("incompat-30/infer-return-type")).incompat30Settings

// Contextual abstraction incompatibilities
lazy val ambiguousConversion =
  project.in(file("incompat-30/ambiguous-conversion"))
    .incompat30Settings
    .settings(scalacOptions += "-language:implicitConversions")
lazy val typeOfImplicitDef = 
  project.in(file("incompat-30/type-of-implicit-def"))
    .incompat30Settings
    .settings(scalafixRewrite := true)
lazy val accessModifier = project.in(file("incompat-30/access-modifier")).incompat30Settings
lazy val viewBound = project.in(file("incompat-30/view-bound"))
  .incompat30Settings
  .settings(scalacOptions += "-deprecation")

// This one is a runtime incompatibility
// It compiles but the runtime behavior is different
// In this case Scala 3 throws an `AssertionError`
lazy val implicitView =
  project.in(file("incompat-30/implicit-view"))
    .runtimeIncompat30Settings
    .settings(scalacOptions += "-language:implicitConversions")

/*
  Dotty migration rewrites between 3.0 and 3.1
  `dotc -source:future-migration -rewrite`
*/
lazy val dotty31MigrationRewrites = Seq[ProjectReference](
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
  project.in(file("incompat-31/alphanumeric-infix"))
    .dotty31MigrationRewriteSettings
    .settings(
      scalacOptions -= "-source:future-migration", 
      scalacOptions ++=  Seq("-source:future", "-deprecation") 
    )

lazy val contextBoundArg = project.in(file("incompat-31/context-bound-arg")).dotty31MigrationRewriteSettings
lazy val etaExpansion = project.in(file("incompat-31/eta-expansion")).dotty31MigrationRewriteSettings
lazy val implicitLambdaParam = project.in(file("incompat-31/implicit-lambda-param")).dotty31MigrationRewriteSettings
lazy val patternBinding = project.in(file("incompat-31/pattern-binding")).dotty31MigrationRewriteSettings
lazy val wildcard = new IncompatSettings(project.in(file("incompat-31/wildcard"))).dotty31MigrationRewriteSettings
