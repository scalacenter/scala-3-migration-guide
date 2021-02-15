## Case class companion

The companion object of a case class does not extend any of the `Function{0-23}` traits anymore.
In particular, it does not inherit their methods: `tupled`, `curried`, `andThen`, `compose`...

For instance, the following lines of code are invalid.

```scala
case class Foo(x: Int, b: Boolean)

Foo.curried(1)(true)
Foo.tupled((2, false))
```

A cross-compiling solution is to explicitly eta-expand the method `Foo.apply`.

```scala
(Foo.apply _).curried(1)(true)
(Foo.apply _).tupled((2, false))
```

Or, for performance reason, you can introduce an intermediate function value.

```scala
val fooCtr: (Int, Boolean) => Foo = (x, b) => Foo(x, b)

fooCtr.curried(1)(true)
fooCtr.tupled((2, false))
```
