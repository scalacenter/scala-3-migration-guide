---
id: migration-tutorial
title: Porting a Macro Library
---

In this tutorial we will learn two different approaches to migrate a macro library to Scala 3.0:
- [Cross-Building](#cross-building)
- [Mixing Macro Definitions in Scala 3.0](#mixing-macro-definitions)

Each approach makes the library available in Scala 3.0 while maintaining Scala 2.13 compatibility.

## A Scala 2 Macro Definition

Let's define a simple Scala 2.13 macro library that contains a single macro method called `location`.

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

Running this application prints `Line 4 in app/src/main/scala/app/Main.scala`, since the call to `location` is made on the 4th line of the file.

So far our sbt project looks like this:

```scala
// build.sbt
lazy val lib = project
  .in(file("lib"))
  .settings(
    scalaVersion := "@scala213@",
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value
    )
  )

lazy val app = project
  .in(file("app"))
  .settings(scalaVersion := "@scala213@")
  .dependsOn(lib)
```

## Moving to Scala 3

> sbt `1.4.+` is required

Let's try to compile our `app` module with Scala 3.0.
First we add the `sbt-dotty` plugin in the `project/plugins.sbt` file, then we change the `scalaVersion` setting of `app` to `@scala30@`.

```scala
// project/plugins.sbt
addSbtPlugin("ch.epfl.lamp" % "sbt-dotty" % "@sbtDotty@")
```

```scala
// build.sbt
lazy val app = project
  .in(file("app"))
  .settings(scalaVersion := "@scala30@")
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

This little example shows that a macro definition must be implemented in Scala 3 to be consumed by Scala 3.0.

At the same time, we want our library to keep being available in Scala 2.13.
There are two solutions for doing so:
- Cross-building
- Mixing macro definitions in Scala 3.0

While the first technique has been known and proven useful for quite some time, the second approach is made possible by TASTy and the brand new Scala 2 TASTy reader.
If you already know how to cross compile you might want to learn about mixing macro definitions by jumping straight to the [Mixing Macro Definition](#mixing-macro-definitions) part of this tutorial.

## Cross-building

Cross-building is a well-known technique to build and publish a project against different Scala versions.

It gives you the ability to:
- Load different binary versions of the same set of dependencies.
- Organize your source code in Scala-version specific source directories.
- Configure and call different instances of the Scala compiler.
- Package and publish the different binaries of your project.

Similar cross-building mechanisms are provided by a number of build tools, among which sbt and Mill.
You can go to the [sbt documentation](https://www.scala-sbt.org/1.x/docs/Cross-Build.html) or the [Mill documentation](https://www.lihaoyi.com/mill/page/cross-builds.html) to familiarize yourself with it.

As we have seen in the [Compatibility Reference](../general/compatibility.md) page, Scala 2.13 and Scala 3.0 binaries are generally compatible, making the cross-building approach not needed in many cases.

However it is still useful in those two cases:
- Maintain support for Scala 2.12 and older versions
- Cross-building a macro library

### 1 - Setting up cross-compilation

We define the desired scala versions of our cross-compiled location module in the `crossScalaVersions` setting.

```scala
// build.sbt
lazy val lib = project
  .in(file("lib"))
  .settings(
    scalaVersion := "@scala213@",
    crossScalaVersions := Seq("@scala213@", "@scala30@"),
    libraryDependencies ++= {
      if (isDotty(scalaVersion.value)) Seq()
      else Seq(
        "org.scala-lang" % "scala-reflect" % scalaVersion.value
      )
    }
  )
```

Note the dependency to `scala-reflect` becomes useless in Scala 3.0.
We use the `isDotty` setting, provided by `sbt-dotty`, to create version specific list of dependencies.

After reloading the sbt shell, we can easily load the Scala 3.0 version of the project by running `++@scala30@`.
Then we can go back to the Scala 2.13 version by running `++@scala213@`.

The sbt `show lib / unmanagedSourceDirectories` command prints the list of available source directories:
```shell
> sbt: location> ++@scala213@; show lib / unmanagedSourceDirectories
[info] * /location/lib/src/main/scala
[info] * /location/lib/src/main/scala-2.13
[info] * /location/lib/src/main/scala-2
[info] * /location/lib/src/main/java
> sbt: location> ++@scala30@; show lib / unmanagedSourceDirectories
[info] * /location/lib/src/main/scala
[info] * /location/lib/src/main/scala-@scala30Binary@
[info] * /location/lib/src/main/scala-3
[info] * /location/lib/src/main/java
```

As we can see, an sbt project defines a shared `scala` source directory and some version-specific source directories.
Some depend on the major version, `scala-2` and `scala-3`, and others depend on the precise binary version, `scala-2.13` and `scala-@scala30Binary@`.

Thus, we can organize our sources depending on their compatibility spectrum.
A piece of code that is compatible and only compatible with all Scala 2 versions of the compiler must go to the `scala-2` directory.
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
> The goal is to reach a state at which the `lib / Compile` and the `lib / Test` artifacts both compile in Scala 2.13 and Scala 3.0.
> 
> Thus you will be able to write the Scala 3.0 code incrementally:
> - Add the Scala 3.0 signature of the wanted method in the `scala-3` folder
> - Move the tests of this method from the `scala-2` folder to the `scala` folder
> - Make the tests pass in Scala 3.0 by providing the implementation
> - Iterate
> 
> You might also want to have some Scala version specific tests that are targeted at the implementation internals.

### 3 - Providing the Scala 3 implementation

We managed to make our `lib` project cross-compile in 2.13 and 3.0.
But the produced Scala 3 binaries do not contain the `location` method.
We can see that by trying to compile the `app` module in Scala 3.0.

Let's add the Scala 3 `location` method.

We add a `Macros.scala` file in the `scala-3` directory and we draw the signature of the `location` method.
**It is worth noting that the signature of the method must be the same in Scala 2 and Scala 3.**

```scala
// lib/src/main/scala-3/location/Macros.scala

package location

object Macros:
  def location: Location = ???
```

This alone should make our `app` module compile in Scala 3.0.

There is no magic formula to port a Scala 2 macro into Scala 3.
One needs to learn about the available new [metaprogramming features](../general/metaprogramming.md).

We eventually come up with this implementation:

```scala
// lib/src/main/scala-3/location/Macros.scala

package location

import scala.quoted.{Quotes, Expr}

object Macros:
  inline def location: Location = ${locationImpl}

  private def locationImpl(using quotes: Quotes): Expr[Location] =
    import quotes.reflect.Position
    val pos = Position.ofMacroExpansion
    val file = Expr(pos.sourceFile.jpath.toString)
    val line = Expr(pos.startLine + 1)
    '{new Location($file, $line)}
```

### Solution overview

Our `lib` project now contains the following files:
- `src/main/scala/location/Location.scala`: The `Location` case class
- `src/main/scala-2/location/Macros.scala`: The Scala 2 implementation of the `location` macro method
- `src/main/scala-3/location/Macros.scala`: The Scala 3 implementation of the `location` macro method

We are now ready to publish our `location` library using the [sbt `publish` task](https://www.scala-sbt.org/1.x/docs/Publishing.html).
It will create two artifacts: the `lib_2.13` and the `lib_3.0` artifacts.

![Cross-building Architecture](assets/macros/cross.svg)

A Scala 2.13 project must depend on the `lib_2.13` artifact whereas a Scala 3.0 project must depend on the `lib_3.0` artifact.

## Mixing Macro Definitions

As it is explained in the [Compatibility Reference](../general/compatibility.md) page, the Scala 2.13 compiler can read the signatures of Scala 3.0 methods, and conversely the Scala 3.0 compiler can read the signatures of Scala 2.13 methods.

A Scala 2 macro implementation cannot be compiled by the Scala 3.0 compiler.
However, there is nothing preventing the Scala 3.0 compiler from type checking a Scala 2 macro definition.

Suppose a Scala 2 macro is defined in a Scala 3.0 module and implemented in a Scala 2.13 module.
Then the Scala 2.13 compiler would be able to find that definition in the Scala 3.0 artifact and execute its implementation in the Scala 2.13 binaries.

This idea sets the ground to the mixing macros technique that we detail further below using the `location` example.
It is compatible with any build tool that can mix Scala 2.13 and Scala 3.0 modules.

### 1 - Creating the Scala 3 module

Going back to the initial state of our `location` library we have:
- A `lib` module that contains the Scala 2 definition of the `location` macro
- A Scala 2.13 `app` module that calls the `location` macros

```scala
// build.sbt

lazy val lib = project
  .in(file("lib"))
  .settings(
    scalaVersion := "@scala213@",
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value
    )
  )

lazy val app = project
  .in(file("app"))
  .settings(scalaVersion := "@scala213@")
  .dependsOn(lib)
```

Let's create a new Scala 3.0 module in which we will move our macro definition.
We call it `macroLib`.

```scala
lazy val macroLib = project
  .in(file("macro-lib"))
  .settings(
    scalaVersion := "@scala30@"
  )
  .dependsOn(lib)
```

To test this new module, we make `app` depend on it. Also we make it cross compile.

```scala
lazy val app = project
  .in(file("app"))
  .settings(
    scalaVersion := "@scala213@",
    crossScalaVersion := Seq("@scala213@", "@scala30@") ,
    scalacOptions ++= {
      if (isDotty.value) Seq()
      else Seq("-Ytasty-reader")
    }
  )
  .dependsOn(macroLib)
```

We are ready to move our Scala 2 macro definition by creating a `Macros.scala` file in `macroLib` that contains the following:

```scala
// macro-lib/src/main/scala/location/Macros.scala
package location

import scala.language.experimental.macros

object Macros:
  def location: Location = macro Scala2Macros.locationImpl
```

The `locationImpl` is still in the `lib` module.
Note that we moved it to a `Scala2Macros` object because we cannot have two `Macros` objects in the same package.

```scala
// lib/src/main/scala/location/Macros.scala

package location

import scala.reflect.macros.blackbox.Context

case class Location(path: String, line: Int) {
  override def toString(): String = s"Line $line in $path"
}

object Scala2Macros {
  def locationImpl(c: Context): c.Tree =  {
    import c.universe._
    val location = typeOf[Location]
    val line = Literal(Constant(c.enclosingPosition.line))
    val path = Literal(Constant(c.enclosingPosition.source.path))
    q"new $location($path, $line)"
  }
}
```

We try to compile our `macroLib` module.

```shell
sbt: location> macroLib / compile
[error] -- Error: /location/macro-lib/src/main/scala/location/Macros.scala:6:6 
[error] 6 |  def location: Location = macro Scala2Macros.locationImpl
[error]   |  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
[error]   |  No Scala 3 implementation found for this Scala 2 macro.
[error] one error found
```

The compiler is telling us that a Scala 3 implementation is missing for the `location` macro.
It makes sense because the Scala 2 implementation cannot be executed by the Scala 3.0 compiler.

Let's add the most simple implementation possible:

```scala
// macro-lib/src/main/scala/location/Macros.scala
package location

import scala.language.experimental.macros

object Macros:
  def location: Location = macro Scala2Macros.locationImpl
  inline def location: Location = ${locationImpl}

  def locationImpl(using ctx: QuoteContext): Expr[Location] = ???
```

The `inline` keyword is required here to tell the compiler the method is the Scala 3 counterpart of the `location` macro definition.

The `app` module can now be compiled and executed in Scala 2.13.

```shell
sbt: location> ++@scala213@; app / run
[info] running app.Main 
Line 6 in /location/app/src/main/scala/app/Main.scala
[info] app / run completed
```

Can the `app` module be compiled in Scala 3.0?
To answer this question we must remind ourselves that macros are executed at compile time.
Here the Scala 3.0 implementation throws a `NotImplementedError`, hence the answer is no.
We can try and see the exception being thrown at compile-time.

```shell
sbt: location> ++@scala30@; app / run
[info] compiling 1 Scala source to /loaction/app/target/scala-@scala30Binary@/classes ...
[error] -- Error: /location/app/src/main/scala/app/Main.scala:6:10 
[error] 6 |  println(location)
[error]   |          ^^^^^^^^
[error]   |          Exception occurred while executing macro expansion.
[error]   |          scala.NotImplementedError: an implementation is missing
[error]   |             at scala.Predef$.$qmark$qmark$qmark(Predef.scala:347)
[error]   |             at location.Macros$.locationImpl(Macros.scala:15)
[error]   |
[error]   | This location contains code that was inlined from Main.scala:6
...
```

### 2 - Providing the Scala 3 implementation

Again, there is no magic formula to port a Scala 2 macro into Scala 3.
One needs to learn about the [new metaprogramming features](../general/metaprogramming.md).

We eventually come up with this implementation:

```scala
// macro-lib/src/main/scala/location/Macros.scala
package location

import scala.language.experimental.macros
import scala.quoted.{Quotes, Expr}

object Macros:
  def location: Location = macro Scala2Macros.locationImpl
  inline def location: Location = ${locationImpl}

  def locationImpl(using quotes: Quotes): Expr[Location] =
    import quotes.reflect.Position
    val file = Expr(Position.ofMacroExpansion.sourceFile.jpath.toString)
    val line = Expr(Position.ofMacroExpansion.startLine + 1)
    '{new Location($file, $line)}
```

The `app` module can now be compiled and executed in Scala 3:

```
sbt: location> ++@scala30@; app / run
[info] running app.Main 
Line 6 in /location/app/src/main/scala/app/Main.scala
[info] app / run completed
```

### Solution Overview

Our `location` library is now composed of two modules:
- The Scala 2.13 `lib` module containing the `Location` data structure and the Scala 2 macro implementation
- The Scala 3.0 `macroLib` module containing both the Scala 2 macro definition and the Scala 3 macro implementation

![Mixing-macros Architecture](assets/macros/mix.svg)

Publishing this library will create two artifacts.
However we now have a single entry point for all projects.
An application can depend on the `macro-lib_3.0` artifact, no matter if its codebase is compiled in Scala 2.13 or Scala 3.0.

## Conclusion

We have learnt two different techniques to make a Scala 2.13 macro library available in Scala 3.0.

When choosing between the two you must take the architecture into consideration:
- When **cross building**, you have one module that produces two Scala version specific artifacts.
The consumer project must find out the right binary version of the library depending on its own Scala version.
- When **mixing macro definitions**, we have a Scala 3.0 module that depends on a Scala 2.13 module.
The consumer project will always depend on the Scala 3.0 artifact irrespective of its own Scala version.
