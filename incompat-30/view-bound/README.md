## View Bounds

View bounds have been deprecated for a long time but they still compile in Scala 2.13.
They do not compile in Scala 3 anymore.

```scala
def foo[A <% Long](a: A): Long = a
```

``` 
-- Error: src/main/scala/view-bound.scala:2:12 
2 |  def foo[A <% Long](a: A): Long = a
  |            ^
  |          view bounds `<%' are deprecated, use a context bound `:' instead
```

The compiler error message suggests we use a context bound instead.

However, in the context of the migration, it may be easier to write the implicit parameter and then call it explicitly, so that the signature of the method is unchanged.

```scala
def foo[A](a: A)(implicit ev: A => Long): Long = ev(a)
```

#### Scala 2.13 deprecation

You can locate the usage of the view bound feature by using the `-deprecation` option of the Scala 2.13 compiler.
It produces messages of the form:

```
src/main/scala/view-bound.scala:2:13: view bounds are deprecated; use an implicit parameter instead.
  example: instead of `def f[A <% Int](a: A)` use `def f[A](a: A)(implicit ev: A => Int)`
  def foo[A <% Long](a: A): Long = a
            ^
```
