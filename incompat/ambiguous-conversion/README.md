This incompatibility is inspired by the [dotty-staging-fork](https://github.com/dotty-staging/scalacheck) of [scalacheck](https://github.com/typelevel/scalacheck) from this [original commit](https://github.com/dotty-staging/scalacheck/commit/dc37c607cd9715c4e0ddc676314c6784cbf2beb5).

## Error message

As of `0.25.0-RC2` the error message is:

```
[error] -- Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/access-modifier/src/main/scala-2.13/access-modifier.scala:4:19
[error] 9 |  true.foo()
[error]   |  ^^^^
[error]   |Found:    (true : Boolean)
[error]   |Required: ?{ foo: ? }
[error]   |Note that implicit extension methods cannot be applied because they are ambiguous;
[error]   |both method boolFoo in object Foo and method lazyBoolFoo in object Foo provide an extension method `foo` on (true : Boolean)
```

## Explanation

In Scala 2 the implicit conversion from `Boolean` wins over the implicit conversion from `=> Boolean`. It is not the case in Scala 3.
