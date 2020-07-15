# Compatibility reference

## Shared ABI

The bytecode and the IR of scala-js, produced by the Scala 2 and Dotty compilers are the same.
It means libraries published for either version can be used on the same classpath.
It enables interoperability and gradual migration, and it relieves us from surprising behavior at runtime.

## Metaprogramming
Compiletime and runtime reflection of programs is incompatible between Scala 2 and Scala 3, so to provide macros with
a common API across versions, the best solution is to [cross build](docs/cross-build.md).
Some community libraries are already cross building macros between 2 and 3, listed in [macros.md](docs/macros.md).
If you instead go all-in with Scala 3, you can still define Scala 2 macros in the same source file as Scala 3 macros,
using the same API, as long as the macro implementation is compiled by Scala 2, (a guide will follow with how to do this).

## Forwards Compatibility
Scala 2 can actually understand many of the new features of Scala 3 as equivalent Scala 2 code.
This means that using many of the new features in an API does not require cross publishing, such as:
  - Enums
  - Intersection types
  - Higher Kinded type lambdas.
  - Opaque type aliases.
  - Scala 3 extension methods.
  - New syntax for implicits.
  - Inheritance of `open` classes and `super` traits.
  - Exported definitions
