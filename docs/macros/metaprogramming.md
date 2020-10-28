---
id: metaprogramming
title: Metaprogramming in Scala 3
---

The Scala 2 macros are compiler-dependent by design. A macros compiled by a version of the compiler cannot be used by another version of the compiler.

Scala 3 is overcoming this limitation by introducing a new principled approach of metaprogramming. While this is an uncontested improvement, it also means that previous macro implementations have to be rewritten from the ground up.

## Before rewriting a macro

Before getting deep into reimplementing a macro your should check if it can be supported using Scala 3 new features.

* Can I encode the logic of the macro using the new scala 3 features?
  * [List of Scala 3 features](http://dotty.epfl.ch/docs/reference/overview.html)
* Can I use *match types* to reimplement the interface of my macro?
  * [Match Types](http://dotty.epfl.ch/docs/reference/new-types/match-types.html)
* Can I use `inline` and the metaprogramming features in `scala.compiletime` to reimplement my logic?
  * [Inline](http://dotty.epfl.ch/docs/reference/metaprogramming/inline.html)
  * [`scala.compiletime`](http://dotty.epfl.ch/api/scala/compiletime/index.html)
* Can I use the simpler and safer expression based macros to implement my macro?
  * [Simple macros](http://dotty.epfl.ch/docs/reference/metaprogramming/macros.html)
* I really need to have access to the raw AST trees
  * [TASTy Reflect](http://dotty.epfl.ch/docs/reference/metaprogramming/tasty-reflect.html)
  * [TASTy inspector](http://dotty.epfl.ch/docs/reference/metaprogramming/tasty-inspect.html)

A good reference for this is [Shapeless 3](https://github.com/dotty-staging/shapeless/tree/shapeless-3). It uses Scala 3 features most of the time for a feature that would have been macros in Scala 2 and only uses macros where absolutely necessary.

## Additional Resources

Documentation:
- [Scala 3 Documentation](https://dotty.epfl.ch/docs/reference/metaprogramming/toc.html)
- [Macros: The Plan For Scala 3](https://www.scala-lang.org/blog/2018/04/30/in-a-nutshell.html)
- [Examples](https://github.com/lampepfl/dotty-macro-examples) - a repository with small, self-contained examples of various tasks done with Scala 3 macros.

Talks:
* [Scala Days - Metaprogramming in Dotty](https://www.youtube.com/watch?v=ZfDS_gJyPTc)

Projects:
* [XML Interpolator](https://github.com/dotty-staging/xml-interpolator/tree/master)
* [Shapeless 3](https://github.com/dotty-staging/shapeless/tree/shapeless-3)

[Contributors welcome!](../contributing.md)
