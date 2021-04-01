---
id: scala-3-migrate-plugin
title: Scala 3 Migrate Plugin
---

## Context

The Scala 3 Migrate plugin is an initiative to make the migration to Scala 3 as easy as possible.
The goal is to help you migrate your build and your code to Scala 3.
The solution consists of 3 independent steps that are packaged in an sbt plugin:

- `migrate-libs`: helps you update the list of `libraryDependencies`
- `migrate-scalacOptions`: helps you update the list of `scalacOptions`
- `migrate-syntax`: fixes a number of syntax incompatibilites in Scala 2.13 code
- `migrate`: tries compiling your code in Scala 3 by adding the minimum required inferred types and implicits.

Each of this step is an sbt command that will be described in details in the following sections.

> #### Requirements
> - Scala 2.13, preferred 2.13.5
> - sbt 1.4 or higher
> - **Disclaimer:** This tool cannot migrate libraries containing macros.
> - **Not implemented yet:**
>   All commands right now work on the `Compile` configuration.
>   Other configurations like `Test` still need to be supported

## Installation

Currently, you can use scala3-migrate via an sbt plugin. You can add it as follows to your build.
```scala
// project/plugins.sbt
addSbtPlugin("ch.epfl.scala" % "sbt-scala3-migrate" % "@scala3Migrate@")
// sbt-dotty is not required since sbt 1.5.0-M1
addSbtPlugin("ch.epfl.lamp" % "sbt-dotty" % "@sbtDotty@")
```

## Choose a module
If your project contains more than one module, the first step is to choose which module to migrate first.

> Scala3-migrate operates on one module at a time. 
> Make sure the module you choose is not an aggregate of projects. 

Follow [this section to choose the first module](../tutorials/sbt-migration.md#1---choose-a-module)

## Migrate library dependencies

> All the commands must be run in an sbt shell

**Usage:** `migrate-libs projectId` where projectId is the name of the module chosen to be migrated.

Let's migrate the following sbt build.
```scala
//build.sbt
lazy val main = project
  .in(file("."))
  .settings(
    name := "main",
    scalaVersion := "2.13.5",
    semanticdbEnabled := true,
    scalacOptions ++= Seq("-explaintypes", "-Wunused"),
    libraryDependencies ++= Seq(
      "org.typelevel"                    %% "cats-core"           % "2.2.0",
      "ch.epfl.scala"                     % "scalafix-interfaces" % "0.9.26",
      "com.softwaremill.scalamacrodebug" %% "macros"              % "0.4.1" % Test,
      "ch.epfl.scala"                    %% "scalafix-rules"      % "0.9.26" % Test,
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full),
      compilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
    )
  )
```

First, let's run the command and see the result.
```scala
> migrate-libs main
[info] Starting to migrate libDependencies for main
[info]
[info] X             : Cannot be updated to scala 3
[info] Valid         : Already a valid version for Scala 3
[info] To be updated : Need to be updated to the following version
[info]
[info] com.softwaremill.scalamacrodebug:macros:0.4.1:test           -> X : Contains Macros and is not yet published for 3.0.0-RC1
[info] com.olegpy:better-monadic-for:0.3.1:plugin->default(compile) -> X : Scala 2 compiler plugins are not supported in scala 3.0.0-RC1. You need to find an alternative
[info] ch.epfl.scala:scalafix-interfaces:0.9.26                     -> Valid
[info] org.typelevel:cats-core:2.2.0                                -> "org.typelevel" %% "cats-core" % "2.4.2"
[info] ch.epfl.scala:scalafix-rules:0.9.26:test                     -> "ch.epfl.scala" % "scalafix-rules_2.13" % "0.9.26" % "test"
[info] org.typelevel:kind-projector:0.11.0:plugin->default(compile) -> -Ykind-projector : This compiler plugin has a scalacOption equivalent. Add it to your scalacOptions
```

### Macro library
```shell
[info] com.softwaremill.scalamacrodebug:macros:0.4.1:test -> X : Contains Macros and is not yet published for 3.0.0-RC1
```
Scala 2.13 macros cannot be executed by the Scala 3 compiler.
So if you depend on a macro lib, you will need to wait until this library is published for Scala 3.

### Compiler plugins
```shell
[info] com.olegpy:better-monadic-for:0.3.1:plugin->default(compile) -> X : Scala 2 compiler plugins are not supported in scala 3.0.0-RC1. You need to find an alternative
[info] org.typelevel:kind-projector:0.11.0:plugin->default(compile) -> -Ykind-projector : This compiler plugin has a scalacOption equivalent. Add it to your scalacOptions
```

`better-monadic-for` is a Scala 2.13 compiler plugin. 
As explained [here](../tutorials/sbt-migration.md#12---compiler-plugins), Scala 2.13 compiler plugins are not supported in Scala 3.
In this case, we need to remove `better-monadic-for` and fix the code to make it compile without the compiler plugin. 

For `kind-projector`, which is also a Scala 2.13 compiler plugin, there is an equivalent scalac option that can be added to your `scalacOptions`.

### Libraries that can be updated
```shell
[info] org.typelevel:cats-core:2.2.0            -> "org.typelevel" %% "cats-core" % "2.4.2"
[info] ch.epfl.scala:scalafix-rules:0.9.26:test -> "ch.epfl.scala" % "scalafix-rules_2.13" % "0.9.26" % "test"
```

- For `cats-core` there is an available version that has been published for `3.0.0-RC1` 
  which is the proposed version `2.4.2`. We can then update the build with this new version.
  
- For `scalafix-rules`, there is no available version for Scala 3, but the library doesn't contain macro
  and therefore the 2.13 version can be used as it is in Scala 3. The syntax still needs to be updated from
 `ch.epfl.scala" `**%%**` "scalafix-rules" % "0.9.26" % "test"` to `ch.epfl.scala" `**%**`"scalafix-rules_2.13" % "0.9.26" % "test"`
  where we explicitly specify the Scala version.
  
There are other syntaxes to express that we want to use the `_2.13` artifact for `3.0.x` code, but keep using
`_2.13` for `2.13.x` and  `_2.12` for `2.12.x`.

if the sbt's version is higher than 1.5:
```scala
"ch.epfl.scala"%% "scalafix-rules" % "0.9.26" % Test cross CrossVersion.for3Use2_13
```
if the sbt's version is 1.3, or 1.4:
```scala
("ch.epfl.scala"%% "scalafix-rules" % "0.9.26" % Test).withDottyCompat(scalaVersion.value)
```

### Valid libraries
```
[info] ch.epfl.scala:scalafix-interfaces:0.9.26 -> Valid
```
Valid libraries are libraries than can be kept as they are. Those libraries are either already in the correct 
version, because they are published in Scala 3.0.0-RC1, or they are Java libraries. 

### The new build after migrate-libs
We use sbt `1.5.0-RC1` and we removed the macro library `"com.softwaremill.scalamacrodebug:macros"` from the libraryDependencies.
When depending on a macro library, you won't be able to migrate to Scala 3, until this library is published 
for Scala 3.

```diff
//build.sbt
- scalacOptions ++= Seq("-explaintypes", "-Wunused"),
+ scalacOptions ++= (if (scalaVersion.value.startsWith("3")) Seq("-explaintypes", "-Wunused", "-Ykind-projector")
                     else Seq("-explaintypes", "-Wunused"))
 libraryDependencies ++= Seq(
-  "org.typelevel"                    %% "cats-core"           % "2.2.0",
+  "org.typelevel"                    %% "cats-core"           % "2.4.2",
  "ch.epfl.scala"                     % "scalafix-interfaces" % "0.9.26",
-  "com.softwaremill.scalamacrodebug" %% "macros"              % "0.4.1" % Test,
-  "ch.epfl.scala"                    %% "scalafix-rules"      % "0.9.26" % Test,
+  ("ch.epfl.scala"                    %% "scalafix-rules"      % "0.9.26" % Test).cross(CrossVersion.for3Use2_13),
-  compilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full),
-  compilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")     
+  ) ++ (CrossVersion.partialVersion(scalaVersion.value) match {
+          case Some((2, 13)) =>
+            Seq(compilerPlugin(("org.typelevel" %% "kind-projector" % V.kindProjector).cross(CrossVersion.full)))
+          case Some((3, _)) => Seq()
+          case _            => Seq()
  })              
```

## Migrate scalacOptions
**Usage:** `migrate-scalacOptions projectId` where projectId is the name of the module chosen to be migrated.

First run the command `migrate-scalacOptions`.
This command rely directly on this [section](../tutorials/scalacoptions-migration.md).
```scala
> migrate-scalacOptions main
[info]
[info] Starting to migrate the scalacOptions for main
[warn] Some scalacOptions are set by sbt plugins and don't need to be modified, removed or added.
[warn] The sbt plugin should adapt its own scalacOptions for Scala 3
[info]
[info] X       : The following scalacOption is specific to Scala 2 and doesn't have an equivalent in Scala 3
[info] Renamed : The following scalacOption has been renamed in Scala3
[info] Valid   : The following scalacOption is a valid Scala 3 option
[info]
[info] -Wunused      -> X
[info] -Yrangepos    -> X
[info] -explaintypes -> -explain-types
[info]
[info] The following scalacOption are specific to compiler plugins, usually added through `compilerPlugin` or `addCompilerPlugin`.
[info] In the previous step `migrate-libs`, you should have removed/fixed compiler plugins and for the remaining plugins and settings, they can be kept as they are.
[info]
[info] -Xplugin:/Users/meriamlachkar/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/typelevel/kind-projector_2.13.3/0.11.0/kind-projector_2.13.3-0.11.0.jar     -> Valid
[info] -Xplugin:/Users/meriamlachkar/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/com/olegpy/better-monadic-for_2.13/0.3.1/better-monadic-for_2.13-0.3.1.jar      -> Valid
[info] -Xplugin:/Users/meriamlachkar/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scalameta/semanticdb-scalac_2.13.3/4.4.0/semanticdb-scalac_2.13.3-4.4.0.jar -> Valid
[info] -P:semanticdb:synthetics:on                                                                                                                                           -> Valid
[info] -P:semanticdb:targetroot:/Users/meriamlachkar/perso/plugin-test/target/scala-2.13/meta                                                                                -> Valid
[info] -P:semanticdb:sourceroot:/Users/meriamlachkar/perso/plugin-test                                                                                                       -> Valid
[info] -P:semanticdb:failures:warning                                                                                                                                        -> Valid
[success]
```
### Non-existing scalacOptions in Scala 3
```scala
[info] -Wunused      -> X
[info] -Yrangepos    -> X
```
- `-Yrangepos`: As explained in the output of this command, some scalacOptions **are not set by you in the build file**, but by
some sbt plugins. For example `scala3-migrate` tool enables semanticdb in Scala 2, which adds `-Yrangepos`. 
Here sbt will adapt the semanticdb options in Scala 3 and therefore there is no need to take any action.
- `-Wunused`: This scalacOption needs to be removed

### Renamed scalacOptions
```scala
-explaintypes -> -explain-types
```
You just need to rename this scalacOption

### Plugins specific scalacOptions
```scala
[info] -Xplugin:/Users/meriamlachkar/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/typelevel/kind-projector_2.13.3/0.11.0/kind-projector_2.13.3-0.11.0.jar     -> Valid
[info] -Xplugin:/Users/meriamlachkar/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/com/olegpy/better-monadic-for_2.13/0.3.1/better-monadic-for_2.13-0.3.1.jar      -> Valid
[info] -Xplugin:/Users/meriamlachkar/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scalameta/semanticdb-scalac_2.13.3/4.4.0/semanticdb-scalac_2.13.3-4.4.0.jar -> Valid
[info] -P:semanticdb:synthetics:on                                                                                                                                           -> Valid
[info] -P:semanticdb:targetroot:/Users/meriamlachkar/perso/plugin-test/target/scala-2.13/meta                                                                                -> Valid
[info] -P:semanticdb:sourceroot:/Users/meriamlachkar/perso/plugin-test                                                                                                       -> Valid
[info] -P:semanticdb:failures:warning 
```
With the previous command `migrate-libs`, we have already adapted `kind-projector` and removed `better-monadic-for` 
for Scala 3. If the previous step is done correctly, you should not need to change anything.

#### Details
- If you have set one of the options above in your build, for example `-P:semanticdb:synthetics:on`, 
  you need to check if this option still exist for the Scala 3 version of this plugin. In this case, `-P:semanticdb:synthetics:on`
  has not been yet implemented in Scala 3 and need to be removed. 
- `better-monadic-for` plugin is in the previous list because we haven't yet `reload` the build.
- `migrate-scalacOptions`: in the previous step, `kind-projector` plugin has been replaced by a scalacOption when it's Scala 3, but 
  `migrate-scalacOptions` retrieves scalacOptions as seen in Scala 2.13. This is why we won't see this scalacOption in
  the list above when we execute `migrate-scalacOptions`. The same way, we will still see the plugin being added through `-Xplugin`.
- `-Xplugin:semanticdb` is added here through an sbt setting `semanticdbEnabled := true` that is set by
  scala3-migrate (this tool). If semanticdb is added through `compilerPlugin` or `addCompilerPlugin`, it will
  be listed as a `libraryDependencies` when we execute `migrate-libs`. The support of SemanticDB is now shipped 
  into the Scala 3 compiler, and will be configured with the same setting: `semanticdbEnabled := true`. 
  Scala3-migrate doesn't enable SemanticDB in Scala 3 unless it's configured in the build.
  

### The new build after migrate-scalacOptions
```diff
- scalacOptions ++= (if (scalaVersion.value.startsWith("3")) Seq("-explaintypes", "-Wunused", "-Ykind-projector")
                     else Seq("-explaintypes", "-Wunused"))
+ scalacOptions ++= (if (scalaVersion.value.startsWith("3")) Seq("-explain-types", "-Ykind-projector")
                     else Seq("-explaintypes", "-Wunused"))                     
```

# Migrate the syntax
**Usage:** `migrate-syntax projectId` where projectId is the name of the module chosen to be migrated.

The command `migrate-syntax` fixes some incompatibilities by applying the following Scalafix rules:

- ProcedureSyntax
- fix.scala213.ConstructorProcedureSyntax
- fix.scala213.ExplicitNullaryEtaExpansion
- fix.scala213.ParensAroundLambda
- fix.scala213.ExplicitNonNullaryApply
- fix.scala213.Any2StringAdd

For more information on the incompatibilities fixed, please read the [incomatibility section](../incompatibilities/table.md).

Let's run the command `migrate-syntax`. If there are changes after this command, it's better if you **create a commit for those.** 
```shell
> migrate-syntax main
[success] Total time: 0 s, completed 12 Mar 2021, 20:55:51
[info] We are going to fix some syntax incompatibilities
[INFO ] migrate.Scala3Migrate.previewSyntaxMigration:80 - Successfully run fixSyntaxForScala3  in 8839 milliseconds
[INFO ] migrate.utils.ScalafixService.fixInPlace:40 - Syntax fixed for Incompat13.scala)
[INFO ] migrate.utils.ScalafixService.fixInPlace:40 - Syntax fixed for Cats6.scala)
[INFO ] migrate.utils.ScalafixService.fixInPlace:40 - Syntax fixed for Cats5.scala)
[info]
[info] The syntax incompatibilities have been fixed on the project main
[info] You can now commit the change!
[info] You can also execute the next command to try to migrate to 3.0.0-RC1
[info]
[info] migrate main
[success]
```

# Migrate the code: last command
> First `reload` the build to take into account the modifications in scalacOptions and libraryDependencies.
>

**Usage:** `migrate projectId` where projectId is the name of the module chosen to be migrated

Scala 3 uses a new type inference algorithm, therefore the Scala 3.0 compiler can infer a different
type than the one inferred by the Scala 2.13 (for more information, read [the type inference section](../incompatibilities/table.md#type-inference)). 
This command goal is to find the necessary types to add in order to make you code compiles.

If the libraries has not been ported correctly, running `migrage projectId` will fail reporting the problematic libraries.
```shell
> migrate main
[info] We are going to migrate your project main to scala 3
[INFO ] migrate.ScalaMigrat.buildMigrationFiles:140 - Found 20 patch candidate(s) in 7 file(s)after 1254 milliseconds
[INFO ] migrate.ScalaMigrat.compileInScala3:115 - Successfully compiled with scala 3 in 5997 milliseconds
[INFO ] migrate.internal.FileMigration.loopUntilNoCandidates:41 - 5 remaining candidate(s)
[INFO ] migrate.internal.FileMigration.loopUntilNoCandidates:41 - 2 remaining candidate(s)
[INFO ] migrate.internal.FileMigration.loopUntilNoCandidates:41 - 1 remaining candidate(s)
[INFO ] migrate.internal.FileMigration.migrate:24 - Found 1 required patch(es) in Incompat7.scala after 4385 milliseconds ms
[INFO ] migrate.internal.FileMigration.loopUntilNoCandidates:41 - 2 remaining candidate(s)
[INFO ] migrate.internal.FileMigration.loopUntilNoCandidates:41 - 1 remaining candidate(s)
[INFO ] migrate.internal.FileMigration.migrate:24 - Found 1 required patch(es) in Incompat5.scala after 2243 milliseconds ms
[INFO ] migrate.internal.FileMigration.loopUntilNoCandidates:41 - 2 remaining candidate(s)
[INFO ] migrate.internal.FileMigration.loopUntilNoCandidates:41 - 1 remaining candidate(s)
[INFO ] migrate.internal.FileMigration.migrate:24 - Found 1 required patch(es) in Incompat3.scala after 302 milliseconds ms
[INFO ] migrate.internal.FileMigration.loopUntilNoCandidates:41 - 1 remaining candidate(s)
[INFO ] migrate.internal.FileMigration.migrate:24 - Found 1 required patch(es) in ReflectiveCall.scala after 773 milliseconds ms
[INFO ] migrate.internal.FileMigration.loopUntilNoCandidates:41 - 4 remaining candidate(s)
[INFO ] migrate.internal.FileMigration.loopUntilNoCandidates:41 - 2 remaining candidate(s)
[INFO ] migrate.internal.FileMigration.loopUntilNoCandidates:41 - 1 remaining candidate(s)
[INFO ] migrate.internal.FileMigration.migrate:24 - Found 1 required patch(es) in Incompat9.scala after 1951 milliseconds ms
[INFO ] migrate.internal.FileMigration.loopUntilNoCandidates:41 - 3 remaining candidate(s)
[INFO ] migrate.internal.FileMigration.loopUntilNoCandidates:41 - 1 remaining candidate(s)
[INFO ] migrate.internal.FileMigration.migrate:24 - Found 1 required patch(es) in PrivateLocalImplicitWithoutType.scala after 313 milliseconds ms
[INFO ] migrate.internal.FileMigration.loopUntilNoCandidates:41 - 3 remaining candidate(s)
[INFO ] migrate.internal.FileMigration.loopUntilNoCandidates:41 - 1 remaining candidate(s)
[INFO ] migrate.internal.FileMigration.migrate:24 - Found 1 required patch(es) in Incompat4.scala after 1225 milliseconds ms
[INFO ] migrate.ScalaMigrat.x$11:67 - Incompat7.scala has been successfully migrated
[INFO ] migrate.ScalaMigrat.x$11:67 - Incompat5.scala has been successfully migrated
[INFO ] migrate.ScalaMigrat.x$11:67 - Incompat9.scala has been successfully migrated
[INFO ] migrate.ScalaMigrat.x$11:67 - PrivateLocalImplicitWithoutType.scala has been successfully migrated
[INFO ] migrate.ScalaMigrat.x$11:67 - Incompat4.scala has been successfully migrated
[INFO ] migrate.ScalaMigrat.x$11:67 - ReflectiveCall.scala has been successfully migrated
[INFO ] migrate.ScalaMigrat.x$11:67 - Incompat3.scala has been successfully migrated
[INFO ] migrate.Scala3Migrate.compileWithRewrite:108 - Finalizing the migration: compiling in scala 3 with -rewrite option
[info]
[info]
[info] main project has successfully been migrated to scala 3.0.0-RC1
[info] You can now commit the change!
[info] You can also execute the compile command:
[info]
[info] main / compile
[success]
```
In this example, 7 files were modified by adding either implicit parameters, implicit conversions or a explicit result types. 
Then the tool compiles one last time in Scala 3  with `-rewrite` scalacOption. 

## What to do next ? 
You can start again with another module `MODULE2`. If `MODULE2` depends on the last module migrated, you need
either to add `-Ytasty-reader` to `MODULE2` scalacOptions, or `reload` or `set MODULE-MIGRATED/scalaVersion := "2.13.5"` 

Once you're done, you can remove `scala3-migrate` from your plugins. 

## Contributions and feedbacks are welcome
The tool is still under development, and **we would love to hear from you.**
Every feedback will help us build a better tool: typos, clearer log messages, better documentation, bug reports, ideas of features,
so please open [GitHub issues](https://github.com/scalacenter/scala3-migrate) or contact us on [gitter](https://gitter.im/scala/center).