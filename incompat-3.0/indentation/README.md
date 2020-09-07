## Wrong indentation

The Scala 3 compiler now requires correct indentation.

The following pieces of code that compiled in Scala 2 does not compile anymore, because of the indentation.

```scala
def bar: (Int, Int) = {
  val foo = 1.0
  val bar = foo
    (1, 1)
}
```

```scala
val foo =
  Vector(1) ++
Vector(2) ++
  Vector(3)
```

We suggest using one of the many formatting tools, among which [scalafmt](https://scalameta.org/scalafmt/), [scalariform](https://github.com/scala-ide/scalariform) or the [IntelliJ Scala formatter](https://www.jetbrains.com/help/idea/reformat-and-rearrange-code.html) and others.

Beware that these tools need proper configuration, and that some of them are opinionated.
They may change the entire code style of your project.
