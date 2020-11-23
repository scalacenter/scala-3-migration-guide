## Abstract Override

In Scala 3, overriding a concrete def with an abstract def causes subclasses to consider the def abstract, whereas in Scala 2 it was considered as concrete.

In the following piece of code, the `bar` method in `C` is considered concrete by the Scala 2.13 compiler but abstract by the Scala 3.0 compiler, causing the following error.

```scala
trait A {
  def bar(x: Int): Int = x + 3
}

trait B extends A {
  def bar(x: Int): Int
}

class C extends B
```

```
[error] -- Error: src/main/scala/abstract-override.scala:11:6
[error] 11 |class C extends B
[error]    |      ^
[error]    |class C needs to be abstract, since def bar(x: Int): Int is not defined
```

This behavior was decided in [Dotty issue #4770](https://github.com/lampepfl/dotty/issues/4770).

An easy fix is simply to remove the abstract def, since in practice it had no effect in Scala 2.
