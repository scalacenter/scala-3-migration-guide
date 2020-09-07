## Implicit Views

This incompatibility is inspired by this [commit](https://github.com/dotty-staging/scalacheck/commit/cb5395297a4178886e87c1c9c35fc0e7cf49c8b0) in the [dotty-staging fork](https://github.com/dotty-staging/scalacheck) of [scalacheck](https://github.com/typelevel/scalacheck).

Scala 3 does not support implicit conversion from an implicit value of the form `implicit val ev: A => B`, making the following piece of code invalid:

```scala
trait Pretty {
  val print: String
}

def pretty[A](a: A)(implicit ev: A => Pretty): String = {
  a.print
}
```

The Dotty compiler denies those conversions, except if the migration mode is on.

Be aware that, if you don't fix the incompatibility, the compiler would look for another conversion from the outer scope and it may find one.
It would result in an undesired behavior at runtime.

The following code illustrates the case:

```scala
trait Pretty {
  val print: String
}

implicit def anyPretty(any: Any): Pretty = new Pretty { val print = "any" }

def pretty[A](a: A)(implicit ev: A => Pretty): String = {
  a.print
}
```

The resolved conversion depends on the compiler mode:
  - `3.0-migration`: the compiler performs the `ev` conversion
  - `3.0`: the compiler cannot perform the `ev` conversion but it can perform the `anyPretty`, which is undesired

One simple fix is to call the conversion explicitly:

```scala
ev(a).print
```
