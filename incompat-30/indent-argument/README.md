## Open Brace Indentation For Passing An Argument

In Scala 2 it is possible to pass an argument after a new line by enclosing it into braces.
Although valid, this style of coding is not encouraged by the [Scala style guide](https://docs.scala-lang.org/style) and it is no longer supported in Scala 3.

This syntax is now invalid:
```scala
test("my test")
{
  assert(1 == 1)
}
```

The preferable solution is to write:

``` scala
test("my test") {
  assert(1 == 1)
}
```

#### Scala 3.0 migration rewrite

The Scala 3.0 migration rewrite is to indent the first line of the block.

```scala
test("my test")
  {
  assert(1 == 1)
}
```

This rule applies to other patterns as well, such as refining a type after a new line.

```scala
type Bar = Foo
  {
  def bar(): Int
}
```
