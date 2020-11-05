---
id: sbt-migration
title: Porting an sbt Project
---

In this tutorial we are going to walk through the migration of an sbt project:
1. Choose a module that is ready to migrate
2. Configure sbt for Scala 3
3. Prepare the dependencies
4. Set up cross-building
5. Configure the Scala 3 compiler
6. Fix the incompatibilities
7. Finalize the migration

> The initial Scala version of the project must be 2.13.+
> 
> sbt version 1.4.+ is required.

## 1. Choose a Module

The [Compatibility Reference](../compatibility.md) shows that the Scala 2.13 and Scala 3.0 binaries are compatible.
Thanks to this we are able to migrate a project one module at a time, in any order.

Therefore, the first step of the migration is to choose a single module that is going to be ported first.
We will proceed with its migration then we will start again with another module.

For some reason, we can even decide to partially migrate the project and let some parts be compiled by Scala 2.13.
It would make no difference at runtime, since the Scala 2.13 and Scala 3 binary representations are the same.

There are a few requirements that a module must meet before being ported:
- It must not depend on a Scala 2 macro library that has not yet been ported to Scala 3.
- It must not use a compiler plugin that has no equivalent in Scala 3.
- It must not depend on `scala-reflect`.

The following paragraphs explain how to check those requirements and, in case they are unmet, what needs to be done.

If you are ready to proceed with the migration you can jump straight to the next part: [Configure sbt for Scala 3](#2-configure-sbt-for-scala-3).

### Macro dependencies

The usage of Scala macro libraries is widespread in the Scala 2 ecosystem.
We can mention as examples the following libraries: 
- [lightbend/scala-logging](https://index.scala-lang.org/lightbend/scala-logging)
- [milessabin/shapeless](https://index.scala-lang.org/milessabin/shapeless)
- [playframework/play-json](https://index.scala-lang.org/playframework/play-json)
- [scalatest/scalatest](https://index.scala-lang.org/scalatest/scalatest)

Many more macro libraries are listed in the [Scala Macro Libraries](../macros/macro-libraries.md) page.

When it comes to the migration to Scala 3, Scala 2 macros are problematic because they cannot be executed by the Scala 3 compiler.
That is why you should make sure the macro libraries on which your project depend have been re-implemented in Scala 3.

You can print the list of direct and transitive dependencies of an sbt project by running the `dependencyTree` task in an sbt shell.

```shell
> <module> / Test / dependencyTree
```

You can compare the printed result with the list of macro libraries in the [Scala Macro Libraries](../macros/macro-libraries.md) page.

Each macro library should be upgraded to a Scala 3 compatible version.
If there is no such version, you are encouraged to get in touch with the macro library maintainers.

If you find any macro library that is not listed in the present guide you are invited to [open an issue](https://github.com/scalacenter/scala-3-migration-guide/issues). By doing so you will help many others to migrate their projects.

#### Example

The dependency to `"scalatest" %% "scalatest" % "3.0.9"` must be upgraded because:
- The `scalatest` API is based on some macro definitions.
- The `3.0.9` version is not published for Scala 3.0.

We can upgrade it to version `3.2.2`, which is cross-published in Scala 2.13 and Scala 3.0.

```scala
libraryDependency += "org.scalatest" %% "scalatest" % "3.2.2"
```

### Compiler plugins

The Scala 2 compiler plugins are not compatible with Scala 3.

Compiler plugins are generally configured in the build.sbt file by one of these settings:

```scala
// build.sbt
libraryDependencies +=
  compilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)

scalacOptions += "-Xplugin:<path-to-sxr>/kind-projector_2.13.3-0.11.0.jar"
```

Some compiler plugins may also be automatically added by an sbt plugin.

You can find all configured compiler plugins by looking at the compiler options of your project.

```scala
sbt:example> show example / Compile / scalacOptions
[info] * -Xplugin:target/compiler_plugins/wartremover_2.13.3-2.4.12.jar
[info] * -Xplugin:target/compiler_plugins/semanticdb-scalac_2.13.3-4.3.20.jar
[info] * -Yrangepos
[info] * -P:semanticdb:targetroot:/example/target/scala-2.13/meta
```

In the above example we can see that two compiler plugins are used: wartremover and semanticdb.
To be able to compile to Scala 3 we must find alternatives or deactivate conditionally on Scala 3.
Alternatives to some common compiler plugins are given below.

#### [SemanticDB](https://scalameta.org/docs/semanticdb/guide.html)

The support of SemanticDB is now shipped into the Scala 3 compiler:
- The `-Ysemanticdb` option activates the generation of semanticDB files.
- The `-semanticdb-target` option can be used to specify the output directory of semanticDB files.

sbt is able to configure SemanticDB automatically, in accordance with the Scala version of the module.
For doing so it takes only one setting: `semanticdbEnabled := true`.

#### [Scala.js](https://www.scala-js.org/)

The Scala.js compilation on Scala 3 does not rely on a compiler plugin anymore.

To compile your Scala.js project in Scala 3 you need the `sbt-scalajs` plugin version `1.3.0` or higher.

```scala
// project/plugins.sbt
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.3.0")
```

#### [Scala Native](https://scala-native.readthedocs.io/en/v0.3.9-docs/)

Scala 3 does not yet support Scala Native.

If you project is cross-built to Scala Native, you can still migrate it to Scala 3 but you have to deactivate the Scala Native compilation on Scala 3.
How to? [Contribution welcome!](../contributing.md)

#### [Kind Projector](https://github.com/typelevel/kind-projector)

The `kind-projector` plugin is not available in Scala 3.

However, Scala 3 introduces some features that makes `kind-projector` not needed in many cases:
- [Type Lambdas](http://dotty.epfl.ch/docs/reference/new-types/type-lambdas.html)
- [Kind Polymorphism](http://dotty.epfl.ch/docs/reference/other-new-features/kind-polymorphism.html)
- Poly Function

Additionally, the `-Ykind-projector` compiler option allows the usage of `*` as a wildcard in type projections.

### Run-time reflection

`scala-reflect` will not be ported to Scala 3 because it exposes Scala 2 compiler internals that do not exist in Scala 3.

If your project depends on `scala-reflect`, or consumes instances of the `Manifest` class, it cannot be compiled by the Scala 3 compiler.
To remedy this situation, you can try to re-implement the corresponding parts of the code, using Java reflection or the [Scala 3 metaprogramming features](../macros/metaprogramming.md).

If `scala-reflect` is transitively added in the class path by a library, you probably need to upgrade that dependency to a Scala 3 compatible version of it.

## 2. Configure sbt for Scala 3

Support for Scala 3 in sbt is brought by the `sbt-dotty` plugin.

This plugin provides sbt with the ability to invoke the Scala 3 compiler.

To configure the plugin, we add this line in the `project/plugins.sbt` file.

```scala
// project/plugins.sbt
addSbtPlugin("ch.epfl.lamp" % "sbt-dotty" % "0.4.5")
```

> Don't forget to `reload` the sbt shell after each change in the `sbt` configuration files.

## 3. Prepare the dependencies

The fact that a Scala 3.0 module can depend on a Scala 2.13 artifact, and vice versa, is unprecedented in the history of the Scala language.
As we will see, it gives a lot more flexibility in terms of dependency management.

Using `%%` for dealing with Scala dependencies has been the norm for quite some time. A dependency on a Scala library is traditionally written:

```scala
// build.sbt
libraryDependencies += "org.typelevel" %% "cats-core" % "2.1.1"
```

The `%%` operator means that the library is dependent on the Scala version:
- if the Scala version is 2.12, the `cats-core_2.12-2.1.1.jar` artifact will be resolved
- if the Scala version is 2.13, the `cats-core_2.13-2.1.1.jar` artifact will be resolved

But suppose I switch the `scalaVersion` from `2.13.3` to `3.0.0-M1`.
When I try to compile, sbt complains that `cats-core_3.0-2.1.1.jar` cannot be found.
Of course it is right, yet I must be able to use `cats-core_2.13-2.1.1.jar` to compile to Scala 3.

### A Scala 3.0 module depending on a Scala 2.13 artifact

They are two ways to configure a Scala 3 module that depends on a Scala 2.13 artifact:

- We can precise the binary version manually:
```scala
libraryDependencies += "org.typelevel" % "cats-core_2.13" % "2.1.1"
```
Note that the `%%` operator is replaced by the `%` operator.
Otherwise sbt would try to resolve `cats-core_2.13_2.13-2.1.1` which is nonsense.

- We can use the `withDottyCompat` extension method brought by `sbt-dotty`:
```scala
libraryDependencies +=
  ("org.typelevel" %% "cats-core" % "2.1.1").withDottyCompat(scalaVersion.value)
```
This method changes the behavior of the `%%` operator on Scala 3 versions only, as described in the below table:

| scalaVersion | binary version |
|-|-|
| `2.12.x` | `_2.12` |
| `2.13.x` | `_2.13` |
| `3.0.x` | `_2.13` |

This is much useful in a repository that is cross-built on many versions.

### A Scala 2.13 module depending on a Scala 3.0 artifact

Conversely a Scala 2.13 module can depend on a Scala 3.0 artifact.

> #### Disclaimer
>
> This will only be possible starting from Scala 2.13.4
> The `-Ytasty-reader compiler flag will be required

We can configure it so by specifying the binary version manually:

```scala
scalaVersion := "2.13.4"
scalacOptions += "-Ytasty-reader"
libraryDependencies += "org.typelevel" % "cats-core_3.0" % "2.2.0"
```

Again you can notice that the `%%` is not used.

### Macro dependencies

Macro dependencies are still tied to a particular version of the compiler.
In such case the `%%` operator is still very much relevant.

For instance, the `sourcecode` library is a cross-published macro library and thus it can be configured this way:
```scala
libraryDependency += "com.lihaoyi" %% "sourcecode" % "0.2.1"
```

### What about Scala.js dependencies?

The `%%%` operator is used in Scala.js projects to automatically resolve artifacts, depending on the Scala and Scala.js versions.

It can be combined with the `withDottyCompat` extension method, as in:
```scala
libraryDependencies +=
  ("org.typelevel" %%% "cats-core" % "2.1.1").withDottyCompat(scalaVersion.value)
```

In a JS-only project, it would be possible to specify the Scala.js binary version manually:
```scala
libraryDependencies +=
  "org.typelevel" % "cats-core_sjs1_2.13" % "2.1.1"
```

### Summing up

An sbt project can use different dependency resolution strategies, based on the binary version of the targeted libraries.

```scala
// build.sbt
val foo = project
  .settings(
    crossScalaVersions ++= Seq("2.13.3", "3.0.0-M1")
    libraryDependencies ++= Seq(
      "org.typelevel" % "cats-core_2.13" % "2.1.1",
      "com.lihaoyi" % "os-lib_3.0" % "0.7.1",
      "com.lihaoyi" %% "sourcecode" % "0.2.1"
    )
  )
```

This sbt configuration is valid on both Scala `2.13.3` and Scala `3.0.0-M1`. 
_(Not quite true, because `os-lib` and `sourcecode` are not yet published on `3.0.0-M1`)_

The table below shows which artifacts are resolved depending on the Scala version:

| scalaVersion | cats-core | os-lib | sourcecode |
|-|-|-|-|
| `2.13.3` | `_2.13` | `_3.0` | `_2.13` |
| `3.0.0-M1` | `_2.13` | `_3.0` | `_3.0` |

## 4. Set up cross-building

The two main challenges of the codebase migration are:
- Make the code compile
- Make sure that the run-time behavior is unchanged

For doing so, we recommend the cross-building strategy, that is to make the code compile in Scala 3.0 while maintaining the compatibility with Scala 2.13.
While this can be tricky sometime, it gives us the ability to fallback to 2.13 and run the tests at any stage of the migration.
That way we can fix the incompatibilities one at a time and be confident that the run-time behavior is unchanged.

Cross-building is easy to configure in sbt.
In the project configuration we add:

```scala
crossScalaVersion ++= Seq("2.13.3", "3.0.0-M1")
```

## 5. Configure the Scala 3 compiler

TODO

## 6. Solve the Incompatibilties

TODO

## 7. Finalize the migration

TODO
