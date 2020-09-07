## Value eta-expansion

Dotty introduces [automatic eta-expansion](https://dotty.epfl.ch/docs/reference/changed-features/eta-expansion-spec.html) which will deprecate the method value syntax `m _`.
Furthermore Dotty does not allow eta-expansion of values to nullary functions anymore.
Thus this piece of code is now illegal:

```scala
val x = 1
val f: () => Int = x _
```

#### Dotty migration rewrite

Compiling with `dotc -source:3.0-migration -rewrite` can rewrite it to:

```scala
val x = 1
val f: () => Int = (() => x)
```

#### Scalafix rule

Alternatively you can use the [`scala/scala-rewrites`](https://index.scala-lang.org/scala/scala-rewrites/scala-rewrites/0.1.2?target=_2.13) rule named `fix.scala213.ExplicitNullaryEtaExpansion`.
