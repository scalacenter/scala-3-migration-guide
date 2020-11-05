---
id: compatibility
title: Compatibility Reference
---
Scala 3 is a game changer in terms of compatibility in the Scala ecosystem that will greatly improve the day-to-day experience of the Scala programmers, starting from the migration.

In this page you will learn about the compatibility between Scala 2.13 and Scala 3.0 at different levels of the program lifetime.
You will then find how to benefit from these levels of compatibility in concrete examples of dependencies between Scala 2 and Scala 3.

## Source Level

A large subset of the Scala 2.13 language is still compatible in Scala 3.0.
Not all of it though, some constructs have been simplified, restricted or dropped altogether.
However those decisions were made for good reasons and by taking care that a good workaround is possible.

In general there is a straightforward cross-compiling solution for every incompatibility, so that the migration from Scala 2 to Scala 3 is easy and smooth.
You can find a corpus of incompatibilities associated with their solutions in the [Incompatibility Table](incompatibilities/table.md).

There is an exception though, which is the new metaprogramming framework that replaces the Scala 2 experimental macros.
Further explanations are given down below.

Setting metaprogramming aside, a Scala 2.13 source code can rather easily be translated into a Scala 3 source code.
Once you have it, you will be able to use the new powerful features of Scala 3, which have no equivalent in Scala 2.
Obviously you will lose the source code forward compatibility by doing so.
But amazingly, you will still have compatibility at compile time and runtime, except for some of the most exotic features.
This is a breakthrough in the Scala programming history.

## Compile Time

In your code you can use public types and terms, and call public methods that are defined in a different module or library.
It works well as long as the type checker, which is the compiler phase that validates the semantic consistency of the code, is able to read the signatures of those types, terms and methods, from the class files containing them.

In Scala 2 the signatures are stored in a dedicated format called the Pickle format.
In Scala 3 the story is a bit different because it relies on the TASTy format which is a lot more than a signature layout.
But, for the purpose of migrating from Scala 2 to Scala 3, only the signatures are useful.

### The Scala 3 Unpicklers

The first piece of good news is that the Scala 3 compiler is already able to read both formats, the Scala 2 Pickle format and the TASTy format, and thus it can type check code that depends on modules or libraries compiled by Scala 2.13 or Scala 3.

### The Scala 2 TASTy Reader

The second piece of good news is that the Scala 2 TASTy reader, which enables consuming Scala 3 libraries in Scala 2.13, is well on track.
It will most certainly be available in Scala 2.13.4 and it will support the following new language features:
- Enums
- Intersection types
- Higher-kinded type lambdas
- Opaque type aliases
- Scala 3 extension methods
- New syntax for context abstraction
- Inheritance of `open` classes and `super` traits
- Exported definitions

You can have a Scala 2 module that depends on a Scala 3 module that uses the new features, and this Scala 3 module can even depend on another Scala 2 module.
Cross-compatibility will not restrain you from using the exciting new features of Scala 3.

In short, we have backward and forward compatibility and so **migration can happen gradually and in any order**.

## Runtime

ABI, which stands for Application Binary Interface, is the representation of Scala code in bytecode or Scala.js IR.
It largely determines the runtime behavior of a piece of code.
Scala 2 and Scala 3 share the same ABI.
A piece of code, provided that the inferred types and implicit resolutions are the same, will produce the same bytecode and eventually have the same behavior at runtime.

Sharing the ABI ensures that Scala 2 and Scala 3 class files can be loaded by the same JVM class loader, and that Scala 2 and Scala 3 `sjsir` files can be linked together by the Scala.js linker.
Furthermore it relieves us from a lot of surprising behaviors at runtime.   

## Metaprogramming

A macro definition produces bytecode that will be executed at compile time.
When you depend on a macro definition, the compiler loads the class file that contains it and then calls your macro method to produce the bytecode of your program, that will be executed at runtime.

The Scala 2 macro mechanisms, which were so far available under the `-experimental` flag, are closely tied to the Scala 2 compiler internals.
Therefore it is impossible to execute them in the Scala 3 compiler.

In contrast, the Scala 3 macros are based on the TASTy format which is designed for stability and compiler independence.
We will most certainly have compatibility for the future Scala versions.
However, the Scala 2 compiler does not fully support TASTy and thus cannot run Scala 3 macros.

In order to publish a common macro API for both languages you have to provide the two distinct implementations.
A technique for having declarations of Scala 2 macro in a Scala 3 artifact is exemplified down below and it is further detailed in the [Macro Migration Tutorial](macros/migration-tutorial.md) section.

## Examples

### A Scala 3 module depending on a Scala 2 artifact

A Scala 3 module can depend on a Scala 2 artifact.

![Scala 3 module depending on a Scala 2 artifact](assets/compatibility/3to2.svg)

This diagram can be translated into sbt:

> sbt `1.4.+` is required

```scala
val scala3 = "0.25.0"
val scala2 = "2.13.3"

lazy val foo = project.in.file("foo")
  .settings(scalaVersion := scala3)
  .dependsOn(bar)

lazy val bar = project.in(file("bar"))
  .settings(scalaVersion := scala2)
```

Or, in case `bar` is a published Scala 2.13 library, we can have:

```scala
val scala3 = "0.25.0"

lazy val foo = project.in.file("foo")
  .settings(
    scalaVersion := scala3,
    libraryDependencies += "org.bar" % "bar_2.13" % "1.0.0"
  )
```

#### The Scala Standard Library

The official standard library for Scala 3.0 is the Scala 2.13 library.
Not only the source code is unchanged but it is even not compiled and published under 3.0.
It could be, but it would be useless because, as we have seen, a Scala 3.0 module can depend on a Scala 2.13 artifact.

Therefore the Scala 3 dependency on the standard library follows this exact same pattern.

![Scala 3 library dependency](assets/compatibility/standardlib.svg)

### A Scala 2 module depending on a Scala 3 artifact

As of `2.13.4` a Scala 2 module will be able to depend on a Scala 3 library by enabling the Tasty reader with `-Ytasty-reader`.
![Scala 2 module depending on a Scala 3 artifact](assets/compatibility/2to3.svg)

This diagram can be translated into sbt:

```scala
val scala3 = "0.25.0"
val scala2 = "2.13.4"

lazy val foo = project.in.file("foo")
  .settings(
    scalaVersion := scala2,
    scalacOptions += "-Ytasty-reader"
  )
  .dependsOn(bar)

lazy val bar = project.in(file("bar"))
  .settings(scalaVersion := scala3)
```

Or, in case `bar` is a published Scala 3 library:

```scala
val scala2 = "2.13.4"

lazy val foo = project.in.file("foo")
  .settings(
    scalaVersion := scala3,
    scalacOptions += "-Ytasty-reader",
    libraryDependencies += "org.bar" % "bar_0.25" % "1.0.0"
  )
```

### Macro dependencies

#### A Scala 3 module cannot depend on a Scala 2 macro

The Scala 3 compiler cannot call a macro definition implemented in a Scala 2 artifact.

![Not working](assets/compatibility/3toMacro2.svg)

But a Scala 3.0 module can depend on a Scala 2.13 artifact whose compilation has involved a Scala 2 macro execution.

![Transitive macro dependency](assets/compatibility/3toMacro2bis.svg)

#### A Scala 2 module cannot depend on a Scala 3 macro

The Scala 2 compiler cannot call a Scala 3 macro definition.

![Not working](assets/compatibility/2toMacro3.svg)

But a Scala 2 module can depend on Scala 3 artifact whose compilation has involved a Scala 3 macro execution.

![Transitive macro dependency](assets/compatibility/2toMacro3bis.svg)

> The macro could have been defined inside the Scala 3 module that consumes it.

#### A shared macro API in a Scala 3 artifact

A Scala 3 artifact can bear the declaration of the Scala 2.13 and Scala 3.0 macros next to one another.
The Scala 3 implementation can be provided in the same module.
The Scala 2 implementation must be compiled by the Scala 2 compiler, and so it must be provided in an other module.

![Shared macro API in a Scala 3 artifact](assets/compatibility/sharedMacroAPI.svg)

This technique makes possible for the Scala 2.13 and Scala 3.0 compilers to execute a macro defined in the same Scala 3.0 artifact.
Yet, each will have its own execution path through the produced bytecode.

You can learn this technique in the [Macro Migration Tutorial](scala-3-migration-mode.md).
Another working example can be found in [this repository](https://github.com/scalacenter/mix-macros-scala-2-and-3).
