Scala 2 reflective calls are dropped and replaced by the broader [programmatic structural types](https://dotty.epfl.ch/docs/reference/changed-features/structural-types.html).

The Scala 2 reflective call behavior can be enabled in Scala 3 by making the `scala.reflect.Selectable.reflectiveSelectable` visible. However the `reflectiveSelectable` term is not available in Scala 2. 

There is a merged PR that will make `reflectiveSelectable` visible in Scala 3 when `scala.language.reflectiveCalls` is imported ([dotty/#9420](https://github.com/lampepfl/dotty/pull/9420)).

In the proposed solution we work around the reflective call by introducing a named type which is not always possible.

## Error message

As of `0.25.0-RC2` the error message is:

``` 
[error] -- [E008] Not Found Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat/reflective-call/src/main/scala-2.13/reflective-call.scala:8:6 
[error] 8 |  foo.bar
[error]   |  ^^^^^^^
[error]   |  value bar is not a member of Object
```
