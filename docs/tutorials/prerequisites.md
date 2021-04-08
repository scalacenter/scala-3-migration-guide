---
id: prerequisites
title: Project Prerequisites
---

The migration to Scala 3 is made easier thanks to the operability between Scala 2.13 and Scala 3, as described in the [Compatibility Reference](../general/compatibility.md) page.

However, there are a few prerequisites that a Scala 2.13 project must meet before being ported to Scala 3:
- It must not depend on a macro library that has not yet been ported to Scala 3.
- It must not use a compiler plugin that has no equivalent in Scala 3.
- It must not depend on `scala-reflect`.

The following paragraphs explain how to check those prerequisites and, in case they are unmet, what you can do about it.

If you are ready to proceed with the migration you can jump straight to the [sbt Migration Tutorial](sbt-migration.md).

### Macro dependencies

A macro library is a Scala library that exposes a macro method.

Those libraries tend to be more expressive and as such their are widely used in Scala 2.
We can mention as examples: 
- [lightbend/scala-logging](https://index.scala-lang.org/lightbend/scala-logging)
- [milessabin/shapeless](https://index.scala-lang.org/milessabin/shapeless)
- [playframework/play-json](https://index.scala-lang.org/playframework/play-json)
- [scalatest/scalatest](https://index.scala-lang.org/scalatest/scalatest)

But the Scala 3 compiler cannot expand Scala 2.13 macros.
So, before jumping to Scala 3, you should make sure that your project does not depend on a macro library that has not yet been ported.

You can find the migration status of many macro libraries in the [Scala Macro Libraries](../macros/macro-libraries.md) page.
Hopefully many will already be ported by the time you read these lines.

For each of these macro dependencies in your project, you need to upgrade it to a cross-built version --- a version available on both Scala 2.13 and Scala 3.

#### Example

The dependency to `"scalatest" %% "scalatest" % "3.0.9"` must be upgraded because:
- The `scalatest` API is based on some macro definitions.
- The `3.0.9` version is not published for Scala 3.

We can upgrade it to version `3.2.7`, which is cross-published in Scala 2.13 and Scala 3.

```scala
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.7"
```

### Compiler plugins

The Scala 2 compiler plugins are not compatible with Scala 3.

Compiler plugins are generally configured in the `build.sbt` file by one of these settings:

```scala
// build.sbt
libraryDependencies +=
  compilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)
```

Some compiler plugins may also be automatically added by an sbt plugin.

You can find all configured compiler plugins by looking at the compiler options of your project.

```shell
sbt:example> show example / Compile / scalacOptions
[info] * -Xplugin:target/compiler_plugins/wartremover_@scala213@-2.4.12.jar
[info] * -Xplugin:target/compiler_plugins/semanticdb-scalac_@scala213@-4.3.20.jar
[info] * -Yrangepos
[info] * -P:semanticdb:targetroot:/example/target/scala-2.13/meta
```

In the above example we can see that two compiler plugins are used: wartremover and semanticdb.
For each of these plugins, we need to check that there is an alternative solution, or we need to disable it.

Alternative solutions to the most used compiler plugins are given below.

#### SemanticDB

The support of [SemanticDB](https://scalameta.org/docs/semanticdb/guide.html) is now shipped into the Scala 3 compiler:
- The `-Ysemanticdb` option activates the generation of semanticDB files.
- The `-semanticdb-target` option can be used to specify the output directory of semanticDB files.

sbt is able to configure SemanticDB automatically with this single setting: `semanticdbEnabled := true`.

#### Scala.js

The [Scala.js](https://www.scala-js.org/) compilation on Scala 3 does not rely on a compiler plugin anymore.

To compile your Scala.js project you can use the `sbt-scalajs` plugin version `1.5.0` or higher.

```scala
// project/plugins.sbt
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.5.0")
```

#### Scala Native

[Scala Native](https://scala-native.readthedocs.io/en/latest/) does not yet support Scala 3.

If you project is cross-built to Scala Native, you can port it to Scala 3.
But you will not be able to compile for the Native platform.

#### Kind Projector

A subset of [the Kind Projector](https://github.com/typelevel/kind-projector) syntax is supported by Scala 3 under the `-Ykind-projector` option.

Additionaly, we now have the following features that make `kind-projector` not needed in many cases:
- [Type Lambdas](http://dotty.epfl.ch/docs/reference/new-types/type-lambdas.html)
- [Kind Polymorphism](http://dotty.epfl.ch/docs/reference/other-new-features/kind-polymorphism.html)
- Poly Function

### Run-time reflection

`scala-reflect` will not be ported to Scala 3 because it exposes Scala 2 compiler internals that do not exist in Scala 3.

If your project depends on `scala-reflect`, or consumes instances of the `Manifest` class, it cannot be compiled by the Scala 3 compiler.
To remedy this situation, you can try to re-implement the corresponding parts of the code, using Java reflection or the [Scala 3 metaprogramming features](../general/metaprogramming.md).

If `scala-reflect` is transitively added in your classpath, you probably need to upgrade the dependency that brings it.
