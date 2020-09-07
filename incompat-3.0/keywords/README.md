## Restricted Keywords

Dotty introduces a number of [keywords](https://dotty.epfl.ch/docs/internals/syntax.html#keywords).
Some are said _regular_ which means that you cannot use them as identifiers anymore.
The others are said _soft_, meaning that they are not restricted.
You can still use the `open` identifier, for instance.

For the matter of migrating from Scala 2 to Scala 3, the keywords that can cause compilation errors are:
- `enum`
- `export`
- `given`
- `then`
- `=>>`
- `?=>`

For instance, the following piece of code does not compile anymore:

```scala
object given {
  val enum = ???

  println(enum)
}
```

There is a straightforward solution which is to backquote the keywords when used as identifiers.

#### Dotty migration rewrite

The Dotty compiler can apply this simple rewrite by itself, with the `-source:3.0-migration -rewrite` options:

```scala
object `given` {
  val `enum` = ???

  println(`enum`)
}
```

In some cases the compiler is not able to apply the solution by itself but then you can add the `-explain` option and it will point you towards the solution.

With the following line of code:

```scala
def then(): Unit = ()
```

It prints the following explanation:

```shell
-- [E040] Syntax Error: src/main/scala/example.scala:2:6 ------------
2 |  def then(): Unit = ()
  |      ^^^^
  |      an identifier expected, but 'then' found

Explanation
===========

If you want to use 'then' as identifier, you may put it in backticks: `then`.
```
