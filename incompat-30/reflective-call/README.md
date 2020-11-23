## Reflective Call

Scala 2 reflective calls are dropped and replaced by the broader [programmatic structural types](https://dotty.epfl.ch/docs/reference/changed-features/structural-types.html).

Scala 3.0 can imitate the Scala 2 reflective calls by making `scala.reflect.Selectable.reflectiveSelectable` wherever `scala.language.reflectiveCalls` is imported.

However the Scala 3.0 compiler does not infer structural types by default, and thus fails at compiling the following piece of code.

```scala
import scala.language.reflectiveCalls

val foo = new {
  def bar: Unit = ???
}

foo.bar
```

The straightforward solution is to give the structural type explicitly.

```scala
val foo: { def bar: Unit }
```
