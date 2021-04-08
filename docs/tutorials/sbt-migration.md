---
id: sbt-migration
title: sbt Migration Tutorial
---

> This tutorial is written for sbt.
> Yet the approach is very similar for any other build tool, as long as it support Scala 3.

Before jumping to Scala 3, make sure you are on the latest Scala 2.13.x and sbt 1.5.x versions.

We are going to walk through the required steps to port an entire project to Scala 3.
- [1. Check the project prerequisites](#1-check-the-project-prerequisites)
- [2. Choose a module](#2-choose-a-module)
- [3. Set up cross-building](#3-set-up-cross-building)
- [4. Prepare the dependencies](#4-prepare-the-dependencies)
- [5. Configure the Scala 3 Compiler](#5-configure-the-scala-3-compiler)
- [6. Solve the Incompatibilities](#6-solve-the-incompatibilities)
- [7. Validate the migration](#7-validate-the-migration)
- [8. Finalize the migration](#8-finalize-the-migration)

## 1. Check the project prerequisites

Make sure your project is ready to be ported:
- It must not depend on a macro library that has not yet been ported to Scala 3.
- It must not use a compiler plugin that has no equivalent in Scala 3.
- It must not depend on `scala-reflect`.

Those prerequisites are described in more details in the [preceding page](prerequisites.md).
## 2. Choose a module

Thanks to the interoperability between Scala 2.13 and Scala 3 you can start with any module.

However it is probably simpler to start with a module that has fewer dependencies.

If you use macro definitions or macros annotations internally you will have to port them first.

## 3. Set up cross-building

The two main challenges of the codebase migration are:
- Make the code compile
- Make sure that the run-time behavior is unchanged

We recommend the cross-building strategy, that is to make the code compile in Scala 3 **and** Scala 2.13.

The reason for this strategy is to be able to run the tests in Scala 2.13 after each fix and thus make sure that the runtime behavior is unchanged.
This is crucial to avoid bugs that could happen when fixing the incompatibilities.

Configuring cross-building ins sbt is as short as:

```scala
scalaVersion := "@scala30@"
crossScalaVersions ++= Seq("@scala213@", "@scala30@")
```

This configuration means:
- The default version is `@scala30@`.
- @scala213@ can be loaded by the `++@scala213@` command.
- @scala30@ can be loaded by the `++@scala30@` command.

Beware that the `reload` command will always load the default version.

## 4. Prepare the dependencies

At this stage, if you run `compile`, it is likely that sbt complains about some dependencies being not found.
That is because the declared version of the dependency is not published for Scala 3.

You either need to upgrade to a newer version or to tell sbt to use the Scala 2.13 version of the library.

You can find the Scala 3 versions of a library in [Scaladex](https://index.scala-lang.org/). To do so, the Version Matrix button can be particularly useful.

> When you change a library dependency, make sure to apply the same change in all modules of your project.

1. If there exists a Scala 3 version of the library

We strongly suggest to use one of the available versions.
Make sure the one you choose does not bring any breaking change.

2. If no such version exists

You can use the Scala 2.13 version of the library. The syntax is:

```scala
("com.lihaoyi" %% "os-lib" % "@osLib@").cross(CrossVersion.for3Use2_13)
```

Or for a Scala.js dependencies:

```scala
("com.lihaoyi" %%% "os-lib" % "@osLib@").cross(CrossVersion.for3Use2_13)
```

Once you have fixed all the unresolved dependencies, you can check that the tests are still passing in Scala 2.13:
```shell
sbt:example> ++@scala213@
sbt:example> test
...
[success]
```
## 5. Configure the Scala 3 Compiler

The Scala 3 compiler options are different from the Scala 2.13 ones: some have been renamed, others are not yet supported.
You can refer to the [Scalac Options Comparative](scalacoptions-migration.md) table to adapt your current list of `scalacOptions`.

A typical configuration looks like this:
```scala
// build.sbt
scalacOptions ++= {
  Seq(
    "-encoding",
    "UTF-8",
    "-feature",
    "-language:implicitConversions",
    // "-Xfatal-warnings" disabled during the migration,
  ) ++ 
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, 13)) => Seq(
        "-deprecation",
        "-Xfatal-warnings",
        "-Wunused:imports,privates,locals",
        "-Wvalue-discard"
      )
      case Some((3, _)) => Seq(
        "-unchecked",
        "-source:3.0-migration"
      )
    }
}
```

Add the `-source:3.0-migration` option to turn on the [Scala 3 Migration Mode](../tooling/scala-3-migration-mode.md).   

> Disable `-Xfatal-warnings` to take full advantage of the migration mode and the automatic rewrites.

## 6. Solve the Incompatibilities

It is now time to try compiling in Scala 3:

```shell
sbt:example> ++@scala30@
[info] Setting Scala version to @scala30@ on 1 project.
...
sbt:example> <project> / compile
...
sbt:example> <project> / Test / compile
```

> `<project> / compile` compiles the `main` sources.
> It is strictly equivalent to `<module> / Compile / compile`.
>
> `<project> / Test / compile` compiles the `test` sources.

The compiler produces diagnostics of two different levels:
- *Error*: A piece of code cannot be compiled anymore.
There are various reasons for those errors, that are given in the [Incompatibility Table](../general/incompatibility-table.md).
- *Migration Warning*: These warnings can be automatically patched by the compiler with the `-rewrite` option.

For each error you can find the corresponding incompatibility in the [Incompatibility Table](../general/incompatibility-table.md).
You will then find a description and a proposed solution.

> The metaprogramming incompatibilities cannot be easily solved.
> A lot of code must be rewritten from the ground up.

After fixing an incompatibility, you can validate the solution in Scala 2.13 by running the tests.

```shell
sbt:example> ++@scala213@
[info] Setting Scala version to @scala213@ on 1 project.
...
sbt:example> <project> / test
...
[success]
```

Consider committing your changes regularly.

Once you have fixed all the errors you should be able to compile succesfully.
Only the migration warnings are remaining.
The compiler can automatically patch them.
Add the `-rewrite` compiler option, and compile one more time:

```shell
sbt:example> ++@scala30@
[info] Setting Scala version to @scala30@ on 1 project.
...
sbt:example> set <project> / scalacOptions += "-rewrite"
[info] Defining <project> / scalacOptions
...
sbt:example> <project> / compile
...
[info] [patched file /example/src/main/scala/app/Main.scala]
[warn] two warnings found
[success]
```

You can remove the `-source:3.0-migration` option, also add the `-Xfatal-warnings` option again if you want to.
Remember to reload.

## 7. Validate the migration

On rare occasions, different implicit values could possibly be resolved and alter the runtime behavior of the program.
Good tests are the only guarantee to prevent such bugs from going unnoticed.

Run the tests with Scala 3.

```shell
sbt:example> <project> / test
...
[success]
```

## 8. Finalize the migration

Congratulations! You have successfully ported a module to Scala 3.
The same process can be repeated for each module, until the project is fully migrated to Scala 3.

You can keep or drop the Scala 2.13 cross-building settings depending on whether you want to cross-publish your library or not.

Here ends our walk through the migration of an sbt project.
