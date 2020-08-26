---
id: macros
title: Macros Migration
---
The Scala 2 macros are compiler dependent by design.
Therefore it is impossible to compile or even consume them in Scala 3.

In this document, we provide guidelines to help you migrate your macro library to Scala 3.

## Migration status

While being experimental, the Scala community has largely adopted the Scala 2 [Def Macro](https://docs.scala-lang.org/overviews/macros/overview.html) feature in multiple of ways: code generation, optimizations, ergonomic DSLs...

Here is an incomplete list of libraries that use Scala 2 macros and their migration status:

* [sourcecode](https://github.com/lihaoyi/sourcecode) – cross-compiles to Scala 2 and Scala 3
* [utest](https://github.com/lihaoyi/utest) – cross-compiles to Scala 2 and Scala 3
* [intent](https://github.com/factor10/intent) - compiles to Scala 3
* [xml-interpolator](https://github.com/lampepfl/xml-interpolator) - compiles to Scala 3
* [expression-evaluator](https://github.com/plokhotnyuk/expression-evaluator) - not migrated, no replacement for `Evals.eval` 
* [jsoniter-scala](https://github.com/plokhotnyuk/jsoniter-scala) - not migrated, no replacement for `Evals.eval`
* [minitest](https://github.com/dotty-staging/minitest) - ported to Scala 3 in the Dotty community build but not merged to upstream
* [munit](https://github.com/dotty-staging/munit) - ported to Scala 3 in the Dotty community build but not merged to upstream
* [scalatest](https://github.com/dotty-staging/scalatest) - ported to Scala 3 in the Dotty community build but not merged to upstream
* [scodec-bits](https://github.com/dotty-staging/scodec) - ported to Scala 3 in the Dotty community build but not merged to upstream

[Contributors welcome!](contributing.md)

## How to?

### Before rewriting a macro

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

### Defining a project that cross compiles macros

If you are already cross-compiling your macro for different versions of Scala 2 and you have sources folders for each version, then you can add an extra source folder for Scala 3.

* SBT examples:
  * *TODO find a simple example project*
* Mill examples
  * *TODO find a simple example project*
  * [utest](https://github.com/dotty-staging/utest/tree/dotty)
  * [sourcecode](https://github.com/dotty-staging/sourcecode/tree/dotty-community-build)

[Contributors welcome!](contributing.md)

## Additional Resources

Documentation:
- [Dotty Documentation](https://dotty.epfl.ch/docs/reference/metaprogramming/toc.html)
- [Macros: The Plan For Scala 3](https://www.scala-lang.org/blog/2018/04/30/in-a-nutshell.html)
- [Examples](https://github.com/lampepfl/dotty-macro-examples) - a repository with small, self-contained examples of various tasks done with Dotty macros.


Talks:
* [Scala Days - Metaprogramming in Dotty](https://www.youtube.com/watch?v=ZfDS_gJyPTc)

Projects:
* [XML Interpolator](https://github.com/dotty-staging/xml-interpolator/tree/master)
* [Shapeless 3](https://github.com/dotty-staging/shapeless/tree/shapeless-3)

[Contributors welcome!](contributing.md)
