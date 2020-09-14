---
id: status
title: Migration Status
---

While being experimental, the Scala community has largely adopted the Scala 2 [Def Macro](https://docs.scala-lang.org/overviews/macros/overview.html) feature in multiple of ways: code generation, optimizations, ergonomic DSLs...

Here is an incomplete list of libraries that use Scala 2 macros and their migration status:

* [expression-evaluator](https://github.com/plokhotnyuk/expression-evaluator) - not migrated, no replacement for `Evals.eval` 
* [intent](https://github.com/factor10/intent) - compiles to Scala 3
* [jsoniter-scala](https://github.com/plokhotnyuk/jsoniter-scala) - not migrated, no replacement for `Evals.eval`
* [minitest](https://github.com/dotty-staging/minitest) - ported to Scala 3 in the Dotty community build but not merged to upstream
* [munit](https://github.com/dotty-staging/munit) - ported to Scala 3 in the Dotty community build but not merged to upstream
* [scalatest](https://github.com/dotty-staging/scalatest) - ported to Scala 3 in the Dotty community build but not merged to upstream
* [scodec-bits](https://github.com/dotty-staging/scodec) - ported to Scala 3 in the Dotty community build but not merged to upstream
* [sourcecode](https://github.com/lihaoyi/sourcecode) – cross-compiles to Scala 2 and Scala 3
* [utest](https://github.com/lihaoyi/utest) – cross-compiles to Scala 2 and Scala 3
* [xml-interpolator](https://github.com/lampepfl/xml-interpolator) - compiles to Scala 3

[Contributors welcome!](../contributing.md)
