---
id: classpath
title: Classpath Level
---

In your code you can use public types and terms, and call public methods that are defined in a different module or library.
It works well as long as the type checker, which is the compiler phase that validates the semantic consistency of the code, is able to read the signatures of those types, terms and methods, from the class files containing them.

In Scala 2 the signatures are stored in a dedicated format called the Pickle format.
In Scala 3 the story is a bit different because it relies on the TASTy format which is a lot more than a signature layout.
But, for the purpose of migrating from Scala 2.13 to Scala 3, only the signatures are useful.

## The Scala 3 Unpickler

The first piece of good news is that the Scala 3 compiler is able to read the Scala 2.13 Pickle format and thus it can type check code that depends on modules or libraries compiled by Scala 2.13.

The Scala 3 unpickler have been extensively tested in the community build.
It is safe to use.

### Using a Scala 2.13 library in Scala 3

A Scala 3 module can depend on a Scala 2.13 artifact.

![Scala 3 module depending on a Scala 2.13 artifact](assets/compatibility/3to2.svg)

As an sbt build it can be reprensented by:

```scala
lazy val foo = project.in.file("foo")
  .settings(scalaVersion := "@scala30@")
  .dependsOn(bar)

lazy val bar = project.in(file("bar"))
  .settings(scalaVersion := "@scala213@)
```

> sbt 1.5.0 or higher is required

Or, in case `bar` is a published Scala 2.13 library, we can have:

```scala
lazy val foo = project.in.file("foo")
  .settings(
    scalaVersion := "@scala30@",
    libraryDependencies += ("org.bar" %% "bar" % "1.0.0").cross(CrossVersion.for3Use2_13)
  )
```

The `CrossVersion.for3Use2_13` is used to tell sbt to resolve `bar_2.13` instead of `bar_3`.

### The Scala Standard Library

**The official standard library for Scala 3 is the Scala 2.13 library.**
It is indeed not needed to recompile the Scala library since a Scala 3 project can depend on a Scala 2.13 artifact.

![Scala 3 library dependency](assets/compatibility/standardlib.svg)

## The Scala 2.13 TASTy Reader

The second piece of good news is that the Scala 2.13 TASTy reader, which enables consuming Scala 3 libraries in Scala 2.13 has been shipped into Scala `@scala213@`.

> The TASTy reader is very new. That's why it is  only available under the `-Ytasty-reader` flag.

It supports all the traditional constructs as well as the following new features:
- [Enumerations](http://dotty.epfl.ch/docs/reference/enums/enums.html)
- [Intersection Types](http://dotty.epfl.ch/docs/reference/new-types/intersection-types.html)
- [Opaque Type Aliases](http://dotty.epfl.ch/docs/reference/other-new-features/opaques.html)
- [Type Lambdas](http://dotty.epfl.ch/docs/reference/new-types/type-lambdas.html)
- [Contextual Abstractions](http://dotty.epfl.ch/docs/reference/contextual/motivation.html) (new syntax)
- [Open Classes](http://dotty.epfl.ch/docs/reference/other-new-features/open-classes.html) (and inheritance of super traits)
- [Export Clauses](http://dotty.epfl.ch/docs/reference/other-new-features/export.html)

We have limited support on:
- [Top-Level Definitions](http://dotty.epfl.ch/docs/reference/dropped-features/package-objects.html)
- [Extension Methods](http://dotty.epfl.ch/docs/reference/contextual/extension-methods.html)

More exotic features are not supported:
- [Context Functions](http://dotty.epfl.ch/docs/reference/contextual/context-functions.html)
- [Polymorphic Function Types](http://dotty.epfl.ch/docs/reference/new-types/polymorphic-function-types.html)
- [Trait Parameters](http://dotty.epfl.ch/docs/reference/other-new-features/trait-parameters.html)
- `@static` Annotation
- `@alpha` Annotation
- [Functions and Tuples larger than 22 parameters](https://dotty.epfl.ch/docs/reference/dropped-features/limit22.html)
- Reference to `scala.Tuple` and `scala.*:`
- [Match Types](https://dotty.epfl.ch/docs/reference/new-types/match-types.html)
- [Union Types](https://dotty.epfl.ch/docs/reference/new-types/union-types.html)
- [Multiversal Equality](https://dotty.epfl.ch/docs/reference/contextual/multiversal-equality.html) (unless explicit)
- [Inline](https://dotty.epfl.ch/docs/reference/metaprogramming/inline.html) (including Scala 3 macros)
- [Kind Polymorphism](https://dotty.epfl.ch/docs/reference/other-new-features/kind-polymorphism.html) (the `scala.AnyKind` upper bound)

### Using a Scala 3 module in a Scala 3

As of `@scala213@` a Scala 2.13 module can depend on a Scala 3 library by enabling the Tasty reader with `-Ytasty-reader`.
![Scala 2 module depending on a Scala 3 artifact](assets/compatibility/2to3.svg)

As an sbt build it can be reprensented by:

```scala
lazy val foo = project.in.file("foo")
  .settings(
    scalaVersion := "@scala213@",
    scalacOptions += "-Ytasty-reader"
  )
  .dependsOn(bar)

lazy val bar = project.in(file("bar"))
  .settings(scalaVersion := "@scala30@")
```

Or, in case `bar` is a published Scala 3 library:

```scala
lazy val foo = project.in.file("foo")
  .settings(
    scalaVersion := "@scala30@",
    scalacOptions += "-Ytasty-reader",
    libraryDependencies += ("org.bar" %% "bar" % "1.0.0").cross(CrossVersion.for2_13Use3)
  )
```

## Intercompatibility Overview

You can have a Scala `@scala213@` module that depends on a Scala `@scala30@` module, and the latter can even depend on another Scala `@scala213@` module.

In short, we have backward and forward compatibility and so **migration can happen gradually**.
An application can jump to Scala 3 even if its dependencies are not fully ported to Scala 3.

> Using the intercompatibility between 2.13 and 3 is useful during the transition period of an application.
> 
> However, it is discouraged to publish a Scala 2.13 library that depends on a Scala 3 library or vice-versa.
> The reason is to prevent library users from ending up with two conflicting version `foo_2.13` and `foo_3` of the same foo library in their classpath.
> 