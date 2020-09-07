## Variance Of Default Parameters

This incompatibility is inspired by [this commit](https://github.com/dotty-staging/geny/commit/61cc4b5dead21b23f664549dfceb5056a2c7e579) in the  [dotty-staging fork](https://github.com/dotty-staging/geny) of the [lihaoyi/geny](https://github.com/lihaoyi/geny) project.

In Scala 2, default parameters are not subject to variance checks which is unsound and might cause runtime failures, as demonstrated by this [test](https://github.com/lampepfl/dotty/blob/10526a7d0aa8910729b6036ee51942e05b71abf6/tests/neg/variances.scala#L1-L20) in the Dotty repository.

Scala 3 does not permit this anymore.

```scala
trait Show[-A] {
  def show(value: List[A] = List.empty): String
}
```

```
-- Error: src/main/scala/default-param-variance.scala:6:28
6 |  def show(value: List[A] = List.empty): String
  |                            ^^^^^^^^^^
  |contravariant type A occurs in covariant position in type => List[A] of method show$default$1
```

In the given example we can fix the issue by providing a default parameter whose type is invariant: `List.empty[Nothing]` instead of `List.empty[A]`.
