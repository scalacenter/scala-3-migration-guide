---
id: macro-mixing
title: Mixing Scala 2.13 and Scala 3 Macros
---

This tutorial shows how to mix Scala 2.13 and Scala 3 macros in a single artifact.

It can be used to create a new Scala 3 macro library and make it available for Scala 2.13 users.

It can also be used to port an existing Scala 2.13 macro library to Scala 3, although it is probably easier to cross-build.

## Introduction

The Scala 2.13 compiler can only expand Scala 2.13 macros and, conversely, the Scala 3 compiler can only expand the Scala 3 macros.

The idea of mixing macros is to provide both definitions in a single artifact, and let the compiler of the consumer project expand the one it supports.

To exemplify this tutorial we will consider the following Scala 3 code skeleton:

```scala
// example/src/main/scala/location/Location.scala
package location

import scala.quoted.{Quotes, Expr}

case class Location(path: String, line: Int)

object Macros:
  def location: Location = macro ???
  inline def location: Location = ${ ??? }
```

As you can see the `location` is defined twice:
- `def location: Location = macro ???` is a Scala 2.13 macro definition
- `inline def location: Location = ${ ??? }` is a Scala 3 macro definition

`location` is not a overloaded method, since both signatures are strictly identical.

This is quite surprising!
How does the compiler accept two methods with the same name and signature?

The reason is that it recognizes the first definition is for Scala 2.13 only and the second is for Scala 3 only.

## 1. The Scala 3 implementation

You can put the Scala 3 macro implementation alongside the definition.

```scala
package location

import scala.quoted.{Quotes, Expr}

case class Location(path: String, line: Int)

object Macros:
  def location: Location = macro ???
  inline def location: Location = ${locationImpl}

  private def locationImpl(using quotes: Quotes): Expr[Location] =
    import quotes.reflect.Position
    val file = Expr(Position.ofMacroExpansion.sourceFile.jpath.toString)
    val line = Expr(Position.ofMacroExpansion.startLine + 1)
    '{new Location($file, $line)}
```

## 2. The Scala 2 implementation

The Scala 3 compiler can compile a Scala 2 macro implementation if it does not contain any quasiquote or reification.

For instance this code does compile in Scala 3 and you can put it alongside the definition and Scala 3 implementation.
```scala
import scala.reflect.macros.blackbox.Context

def locationImpl(c: Context): c.Tree =  {
  import c.universe._
  val line = Literal(Constant(c.enclosingPosition.line))
  val path = Literal(Constant(c.enclosingPosition.source.path))
  New(c.mirror.staticClass(classOf[Location].getName()), path, line)
}
```

However, in many cases you will have to move the Scala 2.13 macro implementation in a Scala 2.13 submodule.

```scala
// build.sbt

lazy val example = project.in(file("example"))
  .settings(
    scalaVersion := "@scala30@"
  )
  .dependsOn(`example-compat`)

lazy val `example-compat` = project.in(file("example-compat"))
  .settings(
    scalaVersion := "@scala213@",
    libraryDependency += "org.scala-lang" % "scala-reflect" % scalaVersion.value
  )
```

In such case we can use quasiquotes.

```scala
package location

import scala.reflect.macros.blackbox.Context
import scala.language.experimental.macros

case class Location(path: String, line: Int)

object Scala2MacrosCompat {
  private[location] def locationImpl(c: Context): c.Tree =  {
    import c.universe._
    val location = typeOf[Location]
    val line = Literal(Constant(c.enclosingPosition.line))
    val path = Literal(Constant(c.enclosingPosition.source.path))
    q"new $location($path, $line)"
  }
}
```

## 3. Validate the implementations

Adding some tests is important to check that the macro method works the same in both Scala versions.

Since we want to execute the tests in Scala 2.13 and Scala 3, we can create a cross-built module:

```scala
// build.sbt
lazy val `example-test` = project.in(file("example-test"))
  .settings(
    scalaVersion := "@scala30@",
    crossScalaVersions := Seq("@scala30@", "@scala213@"),
    scalacOptions ++= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 13)) => Seq("-Ytasty-reader")
        case _ => Seq.empty
      }
    },
    libraryDependencies += "org.scalameta" %% "munit" % "@munit@" % Test
  )
  .dependsOn(example)
```

> `-Ytasty-reader` is needed in Scala 2.13 to consume Scala 3 artifacts

For instance the test can be:
```scala
// example-test/src/test/scala/location/MacrosSpec.scala
package location

class MacrosSpec extends munit.FunSuite {
  test("location") {
    assertEquals(Macros.location.line, 5)
  }
}
```

You should now be able to run the tests in both versions.

```shell
sbt:example> ++@scala213@
sbt:example> example-test / test
location.MacrosSpec:
  + location
[info] Passed: Total 1, Failed 0, Errors 0, Passed 1
[success]
sbt:example> ++@scala30@
sbt:example> example-test / test
location.MacrosSpec:
  + location
[info] Passed: Total 1, Failed 0, Errors 0, Passed 1
[success]
```

## Final Overview

You library is now composed of:
-  The main Scala 3 module containing the mixed macro definitions and the Scala 3 macro implementation.
-  The Scala 2.13 compatibility module containing the Scala 2.13 macro implementation.
It will be used in Scala 2.13 projects during the macro expansion phase.

![Mixing-macros Architecture](assets/macros/mixing.svg)

You are now ready to publish your library.

It can be used in Scala 2.13 projects with these settings:

```scala
scalaVersion := "@scala213@"
libraryDependencies += ("org" %% "example" % "x.y.z").cross(CrossVersion.for2_13Use3)
scalacOptions += "-Ytasty-reader"
```
