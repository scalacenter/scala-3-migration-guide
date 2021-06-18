---
id: macro-libraries
title: Scala Macro Libraries
---

While being experimental, the Scala community has largely adopted the Scala 2 [Def Macro](https://docs.scala-lang.org/overviews/macros/overview.html) feature in multiple of ways: code generation, optimizations, ergonomic DSLs...

A large part of the ecosystem now depends on Scala 2.13 macros defined in external libraries.
Identifying those library dependencies is key to establish that a project is ready to be migrated to Scala 3.

> The Scala 3 compiler cannot execute Scala 2.13 macro definitions.
> 
> However, the handy `-Xignore-scala2-macros` option can be used to ignore them and type check the rest of the code.
> 
> ```scala
> [error] -- Error: /src/main/scala/foo/Foo.scala:10:45 
> [error] 10 |  implicit val foo: Foo[List[Int]] = Foo.make
> [error]    |                                             ^
> [error]    |Scala 2 macro cannot be used in Scala 3. See https://dotty.epfl.ch/docs/reference/dropped-features/macros.html
> [error]    |To turn this error into a warning, pass -Xignore-scala2-macros to the compiler
> ```
>
> Beware that `-Xignore-scala2-macros` will produce bytecode that will fail at runtime.   
>

## Macro Libraries

The following table contains an incomplete list of macro libraries and their migration status.

| Project | Status | Comments |
|-|-|-|
| [adamw/scala-macro-debug](https://index.scala-lang.org/adamw/scala-macro-debug) | <i class="fas fa-times fa-lg"></i> | |
| [ajozwik/quill-generic](https://index.scala-lang.org/ajozwik/quill-generic) | <i class="fas fa-times fa-lg"></i> | |
| [argonaut-io/argonaut](https://index.scala-lang.org/argonaut-io/argonaut) | <i class="fas fa-check  fa-lg"></i> | Since version `6.3.2` |
| [backuity/ansi-interpolator](https://index.scala-lang.org/backuity/ansi-interpolator) | <i class="fas fa-times fa-lg"></i> | |
| [christopherdavenport/log4cats](https://index.scala-lang.org/ChristopherDavenport/log4cats) | <i class="fas fa-times fa-lg"></i> | |
| [circe/circe](https://index.scala-lang.org/circe/circe) | <i class="fas fa-times fa-lg"></i> | `circe-parser` and other modules not yet ported |
| [dmytromitin/auxify](https://index.scala-lang.org/dmytromitin/auxify) | <i class="fas fa-times fa-lg"></i> | |
| [fthomas/refined](https://index.scala-lang.org/fthomas/refined) | <img src="/scala-3-migration-guide/img/tilde.svg"/> | refined macros not yet available |
| [jokade/slogging](https://index.scala-lang.org/jokade/slogging) | <i class="fas fa-times fa-lg"></i> | |
| [getquill/quill](https://index.scala-lang.org/getquill/quill/) | <i class="fas fa-times fa-lg"></i> | |
| [giiita/refuel](https://index.scala-lang.org/giiita/refuel/) | <i class="fas fa-times fa-lg"></i> | |
| [lightbend/scala-logging](https://index.scala-lang.org/lightbend/scala-logging) |  <i class="fas fa-times fa-lg"></i> | |
| [lihaoyi/autowire](https://index.scala-lang.org/lihaoyi/autowire) |  <i class="fas fa-times fa-lg"></i> | |
| [lihaoyi/fastparse](https://index.scala-lang.org/lihaoyi/fastparse) |  <i class="fas fa-times fa-lg"></i> | |
| [lihaoyi/pprint](https://index.scala-lang.org/lihaoyi/pprint) | <i class="fas fa-check fa-lg"></i> | Since version `0.6.0` |
| [lihaoyi/sourcecode](https://index.scala-lang.org/lihaoyi/sourcecode) | <i class="fas fa-check fa-lg"></i> | Since version `0.1.8` |
| [lihaoyi/upickle](https://github.com/lihaoyi/upickle) | <i class="fas fa-check fa-lg"></i> | Since version `1.2.0` |
| [lihaoyi/utest](https://index.scala-lang.org/lihaoyi/utest) | <i class="fas fa-check fa-lg"></i> | Since version `0.7.3` |
| [lloydmeta/enumeratum](https://index.scala-lang.org/lloydmeta/enumeratum) | <i class="fas fa-times fa-lg"></i> | |
| [log4s/log4s](https://index.scala-lang.org/log4s/log4s) | <i class="fas fa-check fa-lg"></i> | Since version `1.10.0-M1` |
| [macro-peg/macro_peg](https://index.scala-lang.org/kmizu/macro_peg) | <i class="fas fa-times fa-lg"></i> | |
| [milessabin/shapeless](https://index.scala-lang.org/milessabin/shapeless) | <i class="fas fa-check  fa-lg"></i> | Being rewritten into Shapeless 3<br/> Since version `3.0.0` ([typelevel/shapeless-3](https://github.com/typelevel/shapeless-3)) |
| [monix/minitest](https://index.scala-lang.org/monix/minitest) | <i class="fas fa-check fa-lg"></i> | Since `2.9.0` |
| [mpollmeier/gremlin-scala](https://index.scala-lang.org/mpollmeier/gremlin-scala) |  <i class="fas fa-times fa-lg"></i> | `macros` module not available for Scala 3.0 |
| [nevillelyh/parquet-avro-extra](https://index.scala-lang.org/nevillelyh/parquet-avro-extra) |  <i class="fas fa-times fa-lg"></i> | |
| [non/imp](https://index.scala-lang.org/non/imp) |  <i class="fas fa-times fa-lg"></i> | |
| [playframework/play-json](https://index.scala-lang.org/playframework/play-json) | <i class="fas fa-times fa-lg"></i> | |
| [plokhotnyuk/expression-evaluator](https://index.scala-lang.org/plokhotnyuk/expression-evaluator) | <i class="fas fa-times fa-lg"></i> | No replacement for `Evals.eval` |
| [plokhotnyuk/fast-string-interpolator](https://index.scala-lang.org/plokhotnyuk/fast-string-interpolator) | <i class="fas fa-times fa-lg"></i> | |
| [plokhotnyuk/jsoniter-scala](https://index.scala-lang.org/plokhotnyuk/jsoniter-scala) | <i class="fas fa-times fa-lg"></i> | No replacement for `Evals.eval` |
| [propensive/magnolia](https://index.scala-lang.org/propensive/magnolia) | <img src="/scala-3-migration-guide/img/tilde.svg"/> | Being rewritten into Magnolia 2<br/> Milestone versions available ([softwaremill/magnolia](https://github.com/softwaremill/magnolia)) |
| [pureconfig/pureconfig](https://index.scala-lang.org/pureconfig/pureconfig) | <i class="fas fa-times fa-lg"></i> | |
| [sangria-graphql/sangria](https://index.scala-lang.org/sangria-graphql/sangria) | <i class="fas fa-times fa-lg"></i> | |
| [scalalandio/chimney](https://index.scala-lang.org/scalalandio/chimney) | <i class="fas fa-times fa-lg"></i> | |
| [scalameta/munit](https://index.scala-lang.org/scalameta/munit) | <i class="fas fa-check fa-lg"></i> | Since `0.3.0` |
| [scalatest/scalatest](https://index.scala-lang.org/scalatest/scalatest) | <i class="fas fa-check fa-lg"></i> | Since version `3.1.0` |
| [scalatest/scalatestplus-junit](https://index.scala-lang.org/scalatest/scalatestplus-junit) | <i class="fas fa-check fa-lg"></i> | Since version `3.1.0` |
| [scodec/scodec-bits](https://index.scala-lang.org/scodec/scodec-bits) | <i class="fas fa-check fa-lg"></i> | Since version `1.1.18` |
| [sirthias/parboiled2](https://index.scala-lang.org/sirthias/parboiled2) | <i class="fas fa-times fa-lg"></i> | |
| [sirthias/borer](https://index.scala-lang.org/sirthias/borer) | <i class="fas fa-times fa-lg"></i> | |
| [slick/slick](https://index.scala-lang.org/slick/slick) | <i class="fas fa-times fa-lg"></i> | |
| [softwaremill/macwire](https://index.scala-lang.org/softwaremill/macwire) | <i class="fas fa-times fa-lg"></i> | |
| [thoughtworksinc/each](https://index.scala-lang.org/thoughtworsinc/each) | <i class="fas fa-times fa-lg"></i> | |
| [typelevel/claimant](https://index.scala-lang.org/typelevel/claimant) | <i class="fas fa-times fa-lg"></i> | |
| [typelevel/spire](https://index.scala-lang.org/typelevel/spire) |  <i class="fas fa-times fa-lg"></i> | |
| [wix/accord](https://index.scala-lang.org/wix/accord) |  <i class="fas fa-times fa-lg"></i> | |
| [wvlet/airframe](https://index.scala-lang.org/wvlet/airframe) |  <i class="fas fa-check fa-lg"></i> | Since version [`20.12.1`](https://wvlet.org/airframe/docs/release-notes#20121) |
| [zio/izumi-reflect](https:://index.scala-lang.org/zio/izumi-reflect) | <i class="fas fa-check fa-lg"></i> | Does not yet support path-dependent type  |
| [zio/zio](https://index.scala-lang.org/zio/zio) | <i class="fas fa-check fa-lg"></i> | Since version [`1.0.4`](https://github.com/zio/zio/releases/tag/v1.0.4) |
| [zero-deps/proto](index.scala-lang.org/zero-deps/proto) | <i class="fas fa-check fa-lg"></i> | Since version [`2.1.0`](https://github.com/zero-deps/proto/releases/tag/2.1) |

If you find any macro library that is not listed here you are invited to [open an issue](https://github.com/scalacenter/scala-3-migration-guide/issues).

## Macro Annotations Libraries

Macro annotations are not supported in Scala 3.
The following table contains a incomplete list of Scala 2.13 libraries that provide macro annotations.
Scala 3 compatible alternatives are proposed when available.

| Project | Alternative Solutions |
|-|-|
| [dmytromitin/auxify](https://index.scala-lang.org/dmytromitin/auxify) | <i class="fas fa-times fa-lg"></i> |
| [julianpeeters/avro-scala-macro-annotations](https://index.scala-lang.org/julianpeeters/avro-scala-macro-annotations) | <i class="fas fa-times fa-lg"></i> |
| [manatki/derevo](https://index.scala-lang.org/manatki/derevo) | <i class="fas fa-times fa-lg"></i> |
| [typelevel/simulacrum](https://index.scala-lang.org/typelevel/simulacrum) | <i class="fas fa-check fa-lg"></i> Source generation using [simulacrum-scalafix](https://index.scala-lang.org/typelevel/simulacrum-scalafix/simulacrum-scalafix/0.5.0?target=_2.12) |

[Contributors welcome!](../contributing.md)
