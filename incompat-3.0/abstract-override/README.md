In Scala 3, overriding a concrete def with an abstract def causes subclasses to consider the def abstract, whereas in Scala 2 it was considered as concrete.
This means that error messages of the form "class C is abstract" can appear when migrating to Scala 3.

This incompatibility is inspired by [this commit](https://github.com/scala-js/scala-js/commit/49f9a8b9f8ce43096d0efb2d5bc4d0e984f6dd8b) in [Scala.js](https://github.com/scala-js/scala-js).

It is related to the [Dotty issue #4770](https://github.com/lampepfl/dotty/issues/4770), where it was decided that the new behavior was intended.

As of `0.25.0-RC2` the error message is
```
[error] -- Error: /localhome/doeraene/projects/scala-migration-guide/incompat/abstract-override/src/main/scala/abstract-override.scala:11:6
[error] 11 |class C extends B
[error]    |      ^
[error]    |class C needs to be abstract, since def bar(x: Int): Int is not defined
```

An easy fix is simply to remove the abstract def, since in practice it had no effect in Scala 2.
