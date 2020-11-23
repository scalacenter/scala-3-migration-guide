This incompatibility is inspired by [this commit](https://github.com/zio/zio/commit/47354b798aaf1969d2980feda1d7bbe829c45429#diff-3c34fedc744e163210b0e536329e4598) from [zio](https://github.com/zio/zio).

As of `0.25.0-RC2` the error message is
```
[error] -- [E007] Type Mismatch Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/type-infer-10/src/main/scala-2.13/type-infer.scala:11:41 
[error] 11 |      case Executing(observer) => notify(observer)
[error]    |                                         ^^^^^^^^
[error]    |                 Found:    (observer : Callback[Option[A$1]])
[error]    |                 Required: Callback[Option[A]]
[error]    |
[error]    |                 where:    A$1 is a type in method test with bounds <: A
```

In the proposed solution the Scala 3 compiler reports the following warning:
```
[warn] -- Warning: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/type-infer-10/src/main/scala/type-infer.scala:11:21 
[warn] 11 |      case Executing(observer: Callback[Option[A]]) => notify(observer)
[warn]    |                     ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
[warn]    |      the type test for Callback[Option[A]] cannot be checked at runtime
```

Precising that `Callback` is covariant in `A` fixes the warning and even more it makes the type annotation useless.
