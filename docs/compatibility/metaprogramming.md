---
id: metaprogramming
title: Metaprogramming
---

A macro is a method that is executed during compilation to produce the code of the program.

The Scala 2.13 macro API is closely tied to the Scala 2.13 compiler internals.
Therefore it is not possible for the Scala 3 compiler to expand any Scala 2.13 macro.

In contrast, Scala 3 introduce a new principled approach of metaprogramming that is designed for stability.
All Scala 3 macros will be compatible with future versions of the Scala 3 compiler.
While this is an uncontested improvement, it also means that Scala 2.13 macro implementations have to be rewritten from the ground up.

## Macro Dependencies

#### A Scala 3 module cannot depend on a Scala 2.13 macro

The Scala 3 compiler cannot execute a macro defined in a Scala 2.13 artifact.

![Not working](assets/compatibility/3toMacro2.svg)

But a Scala 3 module can depend on a Scala 2.13 artifact whose compilation has involved a Scala 2.13 macro expansion.

![Transitive macro dependency](assets/compatibility/3toMacro2bis.svg)

#### A Scala 2.13 module cannot depend on a Scala 3 macro

The Scala 2.13 compiler cannot execute a macro defined in a Scala 3 artifact.

![Not working](assets/compatibility/2toMacro3.svg)

But a Scala 2.13 module can depend on a Scala 3 artifact whose compilation has involved a Scala 3 macro expansion.

![Transitive macro dependency](assets/compatibility/2toMacro3bis.svg)

> The macro can be defined inside the Scala 3 module that consumes it.

## Before Rewriting a Macro

Before getting deep into reimplementing a macro you should check if it can be supported using Scala 3 new features.
- Can I encode the logic of the macro using the new scala 3 features?
- Can I use *match types* to reimplement the interface of my macro?
- Can I use `inline` and the metaprogramming features in `scala.compiletime` to reimplement my logic?
- Can I use the simpler and safer expression based macros to implement my macro?
- Do I really need to access the Abstract Syntax Trees?

You can learn the new metaprogramming concepts by reading the [Macro Tutorial](https://docs.scala-lang.org/scala3/guides/macros/):
- [Inline](https://docs.scala-lang.org/scala3/guides/macros/inline.html)
- [Compile-time operations](https://docs.scala-lang.org/scala3/guides/macros/compiletime.html)
- [Macros](https://docs.scala-lang.org/scala3/guides/macros/macros.html)
- [Quoted code](https://docs.scala-lang.org/scala3/guides/macros/quotes.html)
- [AST Reflection](https://docs.scala-lang.org/scala3/guides/macros/reflection.html)

## Cross-building a Macro Library

You have written a wonderful macro library and you would like it to be available for Scala 2.13 and Scala 3.
There are two different approaches, the traditional cross-building technique and the more recent mixing macros technique.

Both approaches are described in the tutorials section:
- [Cross-Building a Macro Library](../tutorials/macro-cross-building.md)
- [Mixing Scala 2.13 and Scala 3 Macros](../tutorials/macro-mixing.md)

## Additional Resources

Blog posts and talks:
- [Macros: The Plan For Scala 3](https://www.scala-lang.org/blog/2018/04/30/in-a-nutshell.html)
- [Scala Days - Metaprogramming in Dotty](https://www.youtube.com/watch?v=ZfDS_gJyPTc)

Early-adopter projects:
- [XML Interpolator](https://github.com/dotty-staging/xml-interpolator/tree/master)
- [Shapeless 3](https://github.com/dotty-staging/shapeless/tree/shapeless-3)
