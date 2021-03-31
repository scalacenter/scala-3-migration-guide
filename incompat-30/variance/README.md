## Unsoundness Fixes in Variance checks

In Scala 2, default parameters and inner-classes are not subject to variance checks.
It is unsound and might cause runtime failures, as demonstrated by this [test](https://github.com/lampepfl/dotty/blob/10526a7d0aa8910729b6036ee51942e05b71abf6/tests/neg/variances.scala) in the Scala 3 repository.

The Scala 3 compiler does not permit this anymore.

```scala
class Foo[-A](x: List[A]) {
  def f[B](y: List[B] = x): Unit = ???
}

class Outer[+A](x: A) {
  class Inner(y: A)
}
```

```bash
[error] -- Error: src/main/scala/variance.scala:2:8 
[error] 2 |  def f[B](y: List[B] = x): Unit = y
[error]   |        ^^^^^^^^^^^^^^^^^
[error]   |contravariant type A occurs in covariant position in type [B] => List[A] of method f$default$1
[error] -- Error: src/main/scala/variance.scala:6:14 
[error] 6 |  class Inner(y: A)
[error]   |              ^^^^
[error]   |covariant type A occurs in contravariant position in type A of parameter y
```

Each problem of this kind has its own solution, and thus you need to fix them case-by-case.
One of the following options might work for you:
- Make type A invariant
- Add a lower or an upper bound on a type paramater B
- Add a new method overload

In our example, we can opt for these two solutions:

```scala
class Foo[-A](x: List[A]) {
  def f[B](y: List[B]): Unit = ???
  def f(): Unit = f(x)
}

class Outer[+A](x: A) {
  class Inner[B >: A](y: B)
}
```

Or, as a temporary solution, you can also use the `uncheckedVariance` annotation:

```scala
class Outer[+A](x: A) {
  class Inner(y: A @uncheckedVariance)
}
```
