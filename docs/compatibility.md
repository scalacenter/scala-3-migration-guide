---
id: compatibility
title: Compatibility Reference
---
Scala 3 is a game changer in terms of compatibility in the Scala ecosystem that will greatly improve the day-to-day experience of every Scala programmer, starting from the migration.

In this page you will learn about the compatibility between Scala 2.13 and Scala 3 at different stages of the program lifetime.
You will then find out how to benefit from these levels of compatibility in concrete examples of dependencies.

## Source Level

A large subset of the Scala 2.13 language is still compatible in Scala 3.
Not all of it though, some constructs have been simplified, restricted or dropped altogether.
However those decisions were made for good reasons and by taking care that a good workaround is possible.

In general there is a straightforward cross-compiling solution for every incompatibility, so that the migration from Scala 2.13 to Scala 3 is easy and smooth.
You can find a corpus of incompatibilities associated with their solutions in the [Incompatibility Table](incompatibilities/table.md).

There is an exception though, which is the new metaprogramming framework that replaces the Scala 2 experimental macros.
Further explanations are given down below.

Metaprogramming aside, a Scala 2.13 source code can rather easily be ported to Scala 3.
Once done, you will be able to use the new powerful features of Scala 3, which have no equivalent in Scala 2.
The downside is those sources won't compile in Scala 2.13 anymore.
But amazingly, this new Scala 3 artifact can be consumed as a dependency in Scala 2.13.

As we will see in more detail, it permits forward compatibility on many new features, except the most exotic ones.
This is a breakthrough in the Scala programming history.

## Classpath Compatibility

In your code you can use public types and terms, and call public methods that are defined in a different module or library.
It works well as long as the type checker, which is the compiler phase that validates the semantic consistency of the code, is able to read the signatures of those types, terms and methods, from the class files containing them.

In Scala 2 the signatures are stored in a dedicated format called the Pickle format.
In Scala 3 the story is a bit different because it relies on the TASTy format which is a lot more than a signature layout.
But, for the purpose of migrating from Scala 2.13 to Scala 3, only the signatures are useful.

### The Scala 3 Unpicklers

The first piece of good news is that the Scala 3 compiler is already able to read both formats (the Scala 2.13 Pickle format and the TASTy format) and thus it can type check code that depends on modules or libraries compiled by Scala 2.13 or Scala 3.

> These unpicklers have been extensively tested in the Scala 3 community build. They are safe to use.

### The Scala 2.13 TASTy Reader

The second piece of good news is that the Scala 2.13 TASTy reader, which enables consuming Scala 3 libraries in Scala 2.13 has been shipped into Scala `@scala213@`.

> The TASTy reader is very new. As such it is available under the `-Ytasty-reader` flag.

It supports all the traditional constructs as well as the following new features:
- Enumerations
- Intersection types
- Opaque type aliases
- Type lambdas
- New syntax for contextual abstractions
- Inheritance of open classes and super traits
- Exported definitions

We have limited support on:
- Top level definitions
- Extension methods

More exotic features are not supported:
- Context functions
- Polymorphic function types
- Trait parameters
- `@static` annotation
- `@alpha` annotation
- Functions and Tuples larger than 22 parameters
- Reference to `scala.Tuple` and `scala.*:`
- Match types
- Union types
- Multiversal equality constraints unless explicit
- Inline functions (including Scala 3 macros)
- Subtype kind polymorphism (upper bound `scala.AnyKind`)


You can have a Scala `@scala213@` module that depends on a Scala `@scala30@` module, and the latter can even depend on another Scala `@scala213@` module.

In short, we have backward and forward compatibility and so **migration can happen gradually**.
An application can jump to Scala 3 even if its dependencies are not fully ported to Scala 3.

> Using the intercompatibility between 2.13 and 3 is useful during the transition period of an application.
> 
> However, it is discouraged to publish a Scala 2.13 library that depends on a Scala 3 or vice-versa.
> The reason is to prevent library users from ending up with two conflicting version `x_2.13` and `x_3` of the same x library in their classpath.

## Runtime

ABI, which stands for Application Binary Interface, is the representation of Scala code in bytecode or Scala.js IR.
It largely determines the runtime behavior of a piece of code.
Scala 2.13 and Scala 3 share the same ABI.
A piece of code, provided that the inferred types and implicit resolutions are the same, will produce the same bytecode and eventually have the same behavior at runtime.

Sharing the ABI ensures that Scala 2.13 and Scala 3 class files can be loaded by the same JVM class loader, and that Scala 2.13 and Scala 3 `sjsir` files can be linked together by the Scala.js linker.
Furthermore it relieves us from a lot of surprising behaviors at runtime.
It makes the migration from Scala 2.13 to Scala 3 very safe in terms of runtime crashes and performance.

## Metaprogramming

A macro definition produces bytecode that will be executed by the compiler.
When you depend on a macro definition, the compiler loads the class file containing it and calls the macro method to produce the bytecode of your program.

The Scala 2.13 macro feature, which was so far available under the `-experimental` flag, is closely tied to the Scala 2.13 compiler internals.
Therefore it is not possible for the Scala 3 compiler to execute those macros.

In contrast, the Scala 3 macro feature is based on TASTy which is designed for stability and compiler independence.
It will stay compatible with the future versions of the Scala 3 compiler.
But it is not compatible with the Scala 2.13 compiler, which does not fully support TASTy.

In order to publish a common macro API for both languages you must provide both implementations.
A technique for declaring a Scala 2.13 macro in a Scala 3 artifact is exemplified down below and it is further detailed in the [Macro Migration Tutorial](macros/migration-tutorial.md#mixing-macro-definitions) section.

## Examples

> sbt `1.5.0` is required

### A Scala 3 module depending on a Scala 2.13 artifact

A Scala 3 module can depend on a Scala 2.13 artifact.

![Scala 3 module depending on a Scala 2.13 artifact](assets/compatibility/3to2.svg)

This diagram can be translated into sbt:

```scala
lazy val foo = project.in.file("foo")
  .settings(scalaVersion := "@scala30@")
  .dependsOn(bar)

lazy val bar = project.in(file("bar"))
  .settings(scalaVersion := "@scala213@)
```

Or, in case `bar` is a published Scala 2.13 library, we can have:

```scala
lazy val foo = project.in.file("foo")
  .settings(
    scalaVersion := "@scala30@",
    libraryDependencies += ("org.bar" %% "bar" % "1.0.0").cross(CrossVersion.for3Use2_13)
  )
```

#### The Scala Standard Library

The official standard library for Scala 3 is the Scala 2.13 library.
Not only is its source code unchanged but it is even not compiled and published under 3.
It could be but it would be useless because, as we have seen, a Scala 3 module can depend on a Scala 2.13 artifact.

![Scala 3 library dependency](assets/compatibility/standardlib.svg)

### A Scala 2.13 module depending on a Scala 3 artifact

As of `@scala213@` a Scala 2.13 module can depend on a Scala 3 library by enabling the Tasty reader with `-Ytasty-reader`.
![Scala 2 module depending on a Scala 3 artifact](assets/compatibility/2to3.svg)

This diagram can be translated into sbt:

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

### Macro dependencies

#### A Scala 3 module cannot depend on a Scala 2.13 macro

The Scala 3 compiler cannot execute a macro implemented in a Scala 2.13 artifact.

![Not working](assets/compatibility/3toMacro2.svg)

But a Scala 3 module can depend on a Scala 2.13 artifact whose compilation has involved a Scala 2.13 macro execution.

![Transitive macro dependency](assets/compatibility/3toMacro2bis.svg)

#### A Scala 2.13 module cannot depend on a Scala 3 macro

The Scala 2.13 compiler cannot execute a macro implemented in a Scala 3 artifact.

![Not working](assets/compatibility/2toMacro3.svg)

But a Scala 2.13 module can depend on a Scala 3 artifact whose compilation has involved a Scala 3 macro execution.

![Transitive macro dependency](assets/compatibility/2toMacro3bis.svg)

> The macro can be defined inside the Scala 3 module that consumes it.

#### A shared macro API in a Scala 3 artifact

A Scala 3 artifact can bear the declarations of a Scala 3 macro **and** its Scala 2.13 counterpart, next to one another.

![Shared macro API in a Scala 3 artifact](assets/compatibility/sharedMacroAPI.svg)

You can learn this technique in the [Macro Migration Tutorial](macros/migration-tutorial.md#mixing-macro-definitions).
Another working example can be found in [this repository](https://github.com/scalacenter/mix-macros-scala-2-and-3).
