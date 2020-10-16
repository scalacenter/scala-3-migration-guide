---
id: migration-tutorial
title: Migration Tutorial
---

In this tutorial we will learn two different approaches to migrate a macro library to Scala 3: cross-building and mixing macro definitions.
Each approach makes the library available in Scala 3 while maintaining Scala 2 compatibility.

## A Scala 2 Macro Definition

Let's define a simple Scala 2 macro library that contains a single macro method called `location`.

```scala
// lib/src/main/scala/location/Location.scala
package location

import scala.reflect.macros.blackbox.Context
import scala.language.experimental.macros

case class Location(path: String, line: Int) {
  override def toString(): String = s"Line $line in $path"
}

object Macros {
  def location: Location = macro locationImpl

  def locationImpl(c: Context): c.Tree =  {
    import c.universe._
    val location = typeOf[Location]
    val line = Literal(Constant(c.enclosingPosition.line))
    val path = Literal(Constant(c.enclosingPosition.source.path))
    q"new $location($path, $line)"
  }
}
```

This location macro is very handy to capture a position in the code.
We can try it by creating an `app` module that calls the `location` method.

```scala
// app/src/main/scala/app/Main.scala
package app

import location.Macros._

object Main extends App {
  println(location)
}
```

Running this application prints `Line 4 in src/main/scala/SimpleApp.scala`, since the call to `location` is made on the 4th line of the file.

So far our sbt project looks like this:

```scala
// build.sbt

lazy val lib = project
  .in(file("lib"))
  .settings(
    scalaVersion := "2.13.3",
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value
    )
  )

lazy val app = project
  .in(file("app"))
  .settings(scalaVersion := "2.13.3")
  .dependsOn(lib)
```

## Moving to Scala 3

> sbt `1.4.+` is required

Let's try to compile our `app` module with Scala 3.
First we add the `sbt-dotty` plugin in the `project/plugins.sbt` file, then we change the `scalaVersion` setting of `app` to `0.27.0-RC1`.

```scala
// project/plugins.sbt
addSbtPlugin("ch.epfl.lamp" % "sbt-dotty" % "0.4.2")
```

```scala
// build.sbt
lazy val app = project
  .in(file("app"))
  .settings(scalaVersion := "0.27.0-RC1")
  .dependsOn(lib)
```

We reload our project and we try to compile.
It does not work!

```shell
sbt: location> app / compile
[error] -- Error: /location/app/src/main/scala/location/Main.scala:4:10 
[error] 4 |  println(location)
[error]   |          ^
[error]   |Scala 2 macro cannot be used in Dotty. See https://dotty.epfl.ch/docs/reference/dropped-features/macros.html
[error]   |To turn this error into a warning, pass -Xignore-scala2-macros to the compiler
```

This error is pretty self explanatory: Scala 2 macro cannot be used in Scala 3.

We can turn this error into a warning by adding the `-Xignore-scala2-macros` flag in the `scalacOptions`

```shell
sbt: location> set app / scalacOptions += "-Xignore-scala2-macros"
...
sbt: location> app / compile
...
```

Compilation is successful but the compiler warns us the `location` call will crash at runtime, and indeed it does.

This little example shows that a macro definition must be implemented in Scala 3 to be consumed by Scala 3.

At the same time, we want our library to keep being available in Scala 2. There are two solutions for doing so:
- Cross-building
- Mixing macro definitions in Scala 3

While the first technique has been known and proven useful for quite some time, the second approach is made possible by TASTy and the brand new Scala 2 TASTy reader.
If you already know how to cross compile you might want to learn about mixing macro definitions by jumping straight to the [Mixing Macro Definition](#mixing-macro-definitions) part of this tutorial.

## Cross-building

Cross-building is a well-known technique to build and publish a project against different Scala versions.

It gives you the ability to:
- load different binary versions of the same set of dependencies
- organize your source code in Scala-version specific source directories
- configure and call different instances of the Scala compiler
- package and publish the different binaries of your project

Similar cross-building mechanisms are provided by a number of build tools, among which sbt and Mill.
You can go to the [sbt documentation](https://www.scala-sbt.org/1.x/docs/Cross-Build.html) or the [Mill documentation](https://www.lihaoyi.com/mill/page/cross-builds.html) to familiarize yourself with it.

As we have seen in the [Compatibility Reference](../compatibility.md) page, Scala 2.13 and Scala 3.0 binaries are generally compatible, making the cross-building approach not needed in many cases.

However it is still useful in those two cases:
  - maintain support for Scala 2.12 and older versions
  - cross-building a macro library

### 1 - Setting up cross-compilation

We define the desired scala versions of our cross-compiled location module in the `crossScalaVersions` setting.

```scala
// build.sbt
lazy val lib = project
  .in(file("lib"))
  .settings(
    scalaVersion := "2.13.3",
    crossScalaVersions := Seq("2.13.3", "0.27.0-RC1"),
    libraryDependencies ++= {
      if (isDotty(scalaVersion.value)) Seq()
      else Seq(
        "org.scala-lang" % "scala-reflect" % scalaVersion.value
      )
    }
  )
```

Note the dependency to `scala-reflect` becomes useless in Scala 3.
We use the `isDotty` setting, provided by `sbt-dotty`, to create version specific list of dependencies.

After reloading the sbt shell, we can easily load the Scala 3 version of the project by running `++0.27.0-RC1`.
Then we can go back to the Scala 2.13 version by running `++2.13.3`.

The sbt `show lib / unmanagedSourceDirectories` command prints the list of available source directories:
```shell
> sbt: location> ++2.13.3; show lib / unmanagedSourceDirectories
[info] * /location/lib/src/main/scala
[info] * /location/lib/src/main/scala-2.13
[info] * /location/lib/src/main/scala-2
[info] * /location/lib/src/main/java
> sbt: location> ++0.27.0-RC1; show lib / unmanagedSourceDirectories
[info] * /location/lib/src/main/scala
[info] * /location/lib/src/main/scala-0.27
[info] * /location/lib/src/main/scala-3
[info] * /location/lib/src/main/java
```

As we can see, an sbt project defines a shared `scala` source directory and some version-specific source directories.
Some depend on the major version, `scala-2` and `scala-3`, and others depend on the precise binary version, `scala-2.13` and `scala-0.27`.

Thus, we can organize our sources depending on their compatibility spectrum.
A piece of code that is compatible and only compatible with Scala 2 versions of the compiler must go to the `scala-2` directory.
On the other hand, a piece of code that is specific to Scala 2.13 and does not compile with a Scala 2.12 version of the compiler must go to the `scala-2.13` directory.

We are now ready to rearrange our `location` macro.

### 2 - Extracting the Scala 2 implementation

The `lib` module does not compile in Scala 3.0 because it contains a Scala 2 macro implementation.

We need to extract this incompatibility out of the `scala` folder to put it in the Scala 2 specific folder.
We can simply move the `Macros` object to a `Macros.scala` file in the `scala-2` folder.

The `Location` class is perfectly compatible with all versions, we leave it in the `scala` folder to avoid unnecessary duplication of code.

> #### About the tests
> 
> While you are moving some implementations you may also want to move some tests.
> The goal is to reach a state at which the `lib / Compile` and the `lib / Test` artifacts both compile in Scala 2 and Scala 3.
> 
> Thus you will be able to write the Scala 3 code incrementally:
> - Add the Scala 3 signature of the wanted method in the `scala-3` folder
> - Move the tests of this method from the `scala-2` folder to the `scala` folder
> - Make the tests pass in Scala 3 by providing the implementation
> - Iterate
> 
> You might also want to have some Scala version specific tests that are targeted at the implementation internals.

### 3 - Providing the Scala 3 implementation

We managed to make our `lib` project cross-compile in 2.13 and 3.0.
But the produced Scala 3 binaries do not contain the `location` method.
We can see that by trying to compile the `app` module in Scala 3.

Let's add the Scala 3 `location` method.

We add a `Macros.scala` file in the `scala-3` directory and we draw the signature of the `location` method.
**It is worth noting that the signature of the method must be the same in Scala 2 and Scala 3.**

```scala
// lib/src/main/scala-3/location/Macros.scala

package location

object Macros:
  def location: Location = ???
```

This alone should make our `app` module compile in Scala 3.

There is no magic formula to port a Scala 2 macro into Scala 3.
One needs to learn about the available new metaprogramming features of Scala 3.
You can have a look at the list of [available metaprogramming features](metaprogramming-features.md).

We eventually come up with this implementation:

```scala
// lib/src/main/scala-3/location/Macros.scala

package location

import scala.quoted.{QuoteContext, Expr}

object Macros:
  inline def location: Location = ${locationImpl}

  def locationImpl(using ctx: QuoteContext): Expr[Location] =
    import ctx.tasty.rootPosition
    val file = Expr(rootPosition.sourceFile.jpath.toString)
    val line = Expr(rootPosition.startLine + 1)
    '{new Location($file, $line)}
```

### Solution overview

Our `lib` project now contains the following files:
- `src/main/scala/location/Location.scala`: The `Location` case class
- `src/main/scala-2/location/Macros.scala`: The Scala 2 implementation of the `location` macro method
- `src/main/scala-3/location/Macros.scala`: The Scala 3 implementation of the `location` macro method

We are now ready to publish our `location` library using the [sbt `publish` task](https://www.scala-sbt.org/1.x/docs/Publishing.html).
It will create two artifacts: the `2.13` and the `3.0` versions.

## Mixing Macro Definitions

TODO
