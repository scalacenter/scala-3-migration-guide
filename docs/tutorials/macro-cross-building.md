---
id: macro-cross-building
title: Cross-Building a Macro Library
---

Macro libraries must be re-implemented from the ground-up.

Before starting you should already be familiar with Scala 3 migration as described in the [sbt Migration Tutorial](sbt-migration.md)
The purpose of the current tutorial is to cross-build an existing Scala 2.13 macro library so that it becomes available in both Scala 3 and Scala 2.13.

An alternative solution called *Mixing Macros* is explained in the [next tutorial](macro-mixing.md).
You are encourage to read both solutions to choose the technique that is best suited for your need.

## Introduction

In order to exemplify this tutorial, we will consider the minimal macro library defined below.

```scala
// build.sbt
lazy val example = project
  .in(file("example"))
  .settings(
    scalaVersion := "@scala213@",
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value
    )
  )
```

```scala
// example/src/main/scala/location/Location.scala
package location

import scala.reflect.macros.blackbox.Context
import scala.language.experimental.macros

case class Location(path: String, line: Int)

object Macros {
  def location: Location = macro locationImpl

  private def locationImpl(c: Context): c.Tree =  {
    import c.universe._
    val location = typeOf[Location]
    val line = Literal(Constant(c.enclosingPosition.line))
    val path = Literal(Constant(c.enclosingPosition.source.path))
    q"new $location($path, $line)"
  }
}
```

You should recognize some similarities with your library:
one or more macro methods, in our case the `location` method, are implemented by consuming a `Context` and returning a `Tree`.

We can make this library available for Scala 3 users by using the [Cross Building](https://www.scala-sbt.org/1.x/docs/Cross-Build.html) feature in sbt.

The main idea is to built it twice and to publish two artifacts:
- `example_2.13` for Scala 2.13 users
- `example_3` for Scala 3 users

![Cross-building Architecture](assets/macros/cross-building.svg)

## 1. Set up Cross Building

You can add Scala 3 to the list of `crossScalaVersions` of your project:

```scala
crossScalaVersions := Seq("@scala213@", "@scala30@")
```

The `scala-reflect` dependency is not useful in Scala 3.
Remove it conditionally with:

```scala
// build.sbt
libraryDependencies ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, 13)) => Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value
    )
    case _ => Seq.empty
  }
}
```

After reloading sbt, you can switch from the Scala 2.13 context to the Scala 3 context by running `++@scala30@`.
At any point you can go back to the Scala 2.13 context by running `++@scala213@`.

## 2. Rearrange the code in version-specific source files

At this stage you should be able to switch to the Scala 3 context and see that the Scala 3 compiler cannot compile your macro.

You can now rearrange your code in version-specific source directories.
All the code that cannot be compiled by the Scala 3 compiler goes to the the `src/main/scala-2` folder.

In our example, the `Location` class stays in the `src/main/scala` folder but the `Macros` object is moved to the `src/main/scala-2` folder:

```scala
// example/src/main/scala/location/Location.scala
package location

case class Location(path: String, line: Int)
```

```scala
// example/src/main/scala-2/location/Macros.scala
package location

import scala.reflect.macros.blackbox.Context
import scala.language.experimental.macros

object Macros {
  def location: Location = macro locationImpl

  private def locationImpl(c: Context): c.Tree =  {
    import c.universe._
    val location = typeOf[Location]
    val line = Literal(Constant(c.enclosingPosition.line))
    val path = Literal(Constant(c.enclosingPosition.source.path))
    q"new $location($path, $line)"
  }
}
```

We can now initialize each of our Scala 3 macro definitions in the `src/main/scala-3` folder.
They must have the exact same signature than their Scala 2.13 counterparts.

```scala
// example/src/main/scala-3/location/Macros.scala
package location

object Macros:
  def location: Location = ???
```

## 3. Implement the Scala 3 macro

There is no magic formula to port a Scala 2 macro into Scala 3.
One needs to learn about the available new [metaprogramming features](../compatibility/metaprogramming.md).

We eventually come up with this implementation:

```scala
// example/src/main/scala-3/location/Macros.scala
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

## 4. Validate the implementations

Adding some tests is important to check that the macro method works the same in both Scala versions.

In our example, the only test looks like this:

```scala
// example/src/test/scala/location/MacrosSpec.scala
package location

class MacrosSpec extends munit.FunSuite {
  test("location") {
    assertEquals(Macros.location.line, 5)
  }
}
```

You should now be able to run the tests in both versions.

```text
sbt:example> ++@scala213@
sbt:example> example / test
location.MacrosSpec:
  + location
[info] Passed: Total 1, Failed 0, Errors 0, Passed 1
[success]
sbt:example> ++@scala30@
sbt:example> example / test
location.MacrosSpec:
  + location
[info] Passed: Total 1, Failed 0, Errors 0, Passed 1
[success]
```

## Final overview

Your macro project should now contain the following source files:
- `src/main/scala/location/*.scala`: Cross-compatible classes
- `src/main/scala-2/location/*.scala`: The Scala 2 implementation of the macro methods
- `src/main/scala-3/location/*.scala`: The Scala 3 implementation of the macro methods
- `src/test/scala/location/*.scala`: Common tests

![Cross-building Architecture](assets/macros/cross-building.svg)

You are now ready to publish your library.
You will create two releases:
- `example_2.13` for Scala 2.13 users
- `example_3` for Scala 3 users
