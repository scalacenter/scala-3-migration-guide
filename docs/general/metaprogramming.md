---
id: metaprogramming
title: Metaprogramming
---

The Scala 2 macros are compiler-dependent by design.
A macro compiled by one version of the compiler cannot be used by another version of the compiler.

Scala 3 is overcoming this limitation by introducing a new principled approach of metaprogramming.
While this is an uncontested improvement, it also means that previous macro implementations have to be rewritten from the ground up.

## Before Rewriting a Macro

Before getting deep into reimplementing a macro you should check if it can be supported using Scala 3 new features.

- Can I encode the logic of the macro using the new scala 3 features?
- Can I use *match types* to reimplement the interface of my macro?
- Can I use `inline` and the metaprogramming features in `scala.compiletime` to reimplement my logic?
- Can I use the simpler and safer expression based macros to implement my macro?
- Do I really need to have access to the raw AST trees?

## Macro Reference and Tutorial

You can find the references to these new concepts in the [Scala 3 Reference](https://dotty.epfl.ch/docs/reference/metaprogramming/toc.html) website.

Or you can opt for a more pratical approach by following the [Macro Tutorial](https://docs.scala-lang.org/scala3/guides/macros/).
It shows and explains how to use these features:
- [Inline](https://docs.scala-lang.org/scala3/guides/macros/inline.html)
- [Compile-time operations](https://docs.scala-lang.org/scala3/guides/macros/compiletime.html)
- [Macros](https://docs.scala-lang.org/scala3/guides/macros/macros.html)
- [Quoted code](https://docs.scala-lang.org/scala3/guides/macros/quotes.html)
- [AST Reflection](https://docs.scala-lang.org/scala3/guides/macros/reflection.html)

## Cross-building a Macro Library

You have written a wonderful macro library and you would like it to be available for Scala 2.13 and Scala 3.
There are two different approaches, the traditional cross-building technique and the more recent mixing definition technique.

Both approaches are described in the [Porting a Macro Library](../macros/migration-tutorial.md) tutorial.

## Additional Resources

Blog posts and talks:
- [Macros: The Plan For Scala 3](https://www.scala-lang.org/blog/2018/04/30/in-a-nutshell.html)
- [Scala Days - Metaprogramming in Dotty](https://www.youtube.com/watch?v=ZfDS_gJyPTc)

Early-adopter projects:
- [XML Interpolator](https://github.com/dotty-staging/xml-interpolator/tree/master)
- [Shapeless 3](https://github.com/dotty-staging/shapeless/tree/shapeless-3)
