---
id: metaprogramming
title: Metaprogramming in Scala 3
---

The Scala 2.13 macros are compiler-dependent by design.
A macro compiled by one version of the compiler cannot be used by another version of the compiler.

Scala 3.0 is overcoming this limitation by introducing a new principled approach of metaprogramming.
While this is an uncontested improvement, it also means that previous macro implementations have to be rewritten from the ground up.

## Before rewriting a macro

Before getting deep into reimplementing a macro your should check if it can be supported using Scala 3.0 new features.

* Can I encode the logic of the macro using the new scala 3 features?
* Can I use *match types* to reimplement the interface of my macro?
* Can I use `inline` and the metaprogramming features in `scala.compiletime` to reimplement my logic?
* Can I use the simpler and safer expression based macros to implement my macro?
* Do I really need to have access to the raw AST trees?

You can find references to these concepts in the _macro tutorial_.

## Macro turorial

* [Scala 3 Macro Tutorial](https://lampepfl.github.io/scala3-macro-tutorial/docs/tutorial/introduction.html)
  * [Inline](https://lampepfl.github.io/scala3-macro-tutorial/docs/tutorial/inline.html)
  * [Compile-time operations](https://lampepfl.github.io/scala3-macro-tutorial/docs/tutorial/compile-time-operations.html)
  * [Macros](https://lampepfl.github.io/scala3-macro-tutorial/docs/tutorial/scala-3-macros.html)
  * [Quoted code](https://lampepfl.github.io/scala3-macro-tutorial/docs/tutorial/quoted-code.html)
  * [AST Reflection](https://lampepfl.github.io/scala3-macro-tutorial/docs/tutorial/tasty-reflection.html)
## Additional Resources

Documentation:
- [Scala 3.0 Documentation](https://dotty.epfl.ch/docs/reference/metaprogramming/toc.html)
- [Macros: The Plan For Scala 3](https://www.scala-lang.org/blog/2018/04/30/in-a-nutshell.html)
- [Examples](https://github.com/lampepfl/dotty-macro-examples) - a repository with small, self-contained examples of various tasks done with Scala 3 macros.

Talks:
* [Scala Days - Metaprogramming in Dotty](https://www.youtube.com/watch?v=ZfDS_gJyPTc)

Projects:
* [XML Interpolator](https://github.com/dotty-staging/xml-interpolator/tree/master)
* [Shapeless 3](https://github.com/dotty-staging/shapeless/tree/shapeless-3)

[Contributors welcome!](../contributing.md)
