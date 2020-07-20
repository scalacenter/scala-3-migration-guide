This incompatibility is inspired by the [dotty-staging fork](https://github.com/dotty-staging/geny) of [geny](https://github.com/lihaoyi/geny) from this [original commit](https://github.com/dotty-staging/geny/commit/61cc4b5dead21b23f664549dfceb5056a2c7e579).

## Error messages

As of `0.25.0-RC2` the error messages are:

```
[error] -- Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/default-param-variance/src/main/scala-2.13/default-param-variance.scala:2:30
[error] 2 |  def count(f: A => Boolean = _ => true): Int
[error]   |                              ^^^^^^^^^
[error]   |covariant type A occurs in contravariant position in type => A => Boolean of method count$default$1
```
```
[error] -- Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/default-param-variance/src/main/scala-2.13/default-param-variance.scala:6:28
[error] 6 |  def show(value: List[A] = List.empty): String
[error]   |                            ^^^^^^^^^^
[error]   |contravariant type A occurs in covariant position in type => List[A] of method show$default$1
```

## Explanation

A default paramater is a term that is passed to the method when the argument is not explicitly specified. In Scala 2, default parameters are not subject to variance checks which is unsound and might cause runtime failure (example [here](https://github.com/lampepfl/dotty/blob/10526a7d0aa8910729b6036ee51942e05b71abf6/tests/neg/variances.scala#L1-L20)).

The Dotty compiler resolves this issue by checking the variance of default parameters.
