This incompatibility is inspired by the [dotty-staging fork](https://github.com/dotty-staging/scalacheck) of [scalacheck](https://github.com/typelevel/scalacheck) in this [original commit](https://github.com/dotty-staging/scalacheck/commit/cb5395297a4178886e87c1c9c35fc0e7cf49c8b0).

Dotty does not support implicit conversion from an implicit value of the form `implicit val ev: A => B`.

When it comes to migration it can prevent from compiling, but sometimes the compiler will find and pick another conversion, which will result in a different runtime behavior. The present example illustrates the latter case.

As of `0.24.0-RC1` the migration mode of the Dotty compiler (`-source:3.0-migration`) does support those implicit conversions and thus preserving the runtime behavior compare to Scala 2. The downside of it is that the runtime behavior may change between the Scala 3 migration mode and standard mode.
