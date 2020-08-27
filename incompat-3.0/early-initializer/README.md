The early initializers feature has been dropped, as explained [here](https://dotty.epfl.ch/docs/reference/dropped-features/early-initializers.html).

In Scala 3, trait parameters eliminate the need of early initializers. But trait parameters do not compile in Scala 2. In the proposed solution, we introduce an intermediate abstract class to carry the early initializers as constructor parameters.

As of `0.25.0-RC2` the error messages are:

```
[error] -- Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/early-initializer/src/main/scala-2.13/early-initializer.scala:6:19 
[error] 6 |object Foo extends {
[error]   |                   ^
[error]   |                   `extends` must be followed by at least one parent
```

```
[error] -- [E009] Syntax Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/early-initializer/src/main/scala-2.13/early-initializer.scala:8:2 
[error] 8 |} with Bar
[error]   |  ^^^^
[error]   |  Early definitions are not supported; use trait parameters instead
```
