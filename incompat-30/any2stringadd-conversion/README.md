## `any2stringadd` conversion

The implicit `Predef.any2stringadd` conversion is deprecated since `2.13` and dropped in Scala 3.

#### Scala 2.13 deprecation

Given this piece of code:

```scala
val str = new AnyRef + "foo"
```

With the `-deprecation` flag, the Scala 2.13 compiler produces the following warning.

```
src/main/scala/any2stringadd-conversion.scala:2:13: method any2stringadd in object Predef is deprecated (since 2.13.0): Implicit injection of + is deprecated. Convert to String to call +
  val str = new AnyRef + "foo"
```

#### Scalafix rule

The [`scala/scala-rewrites`](https://index.scala-lang.org/scala/scala-rewrites/scala-rewrites/0.1.2?target=_2.13) rule named `fix.scala213.Any2StringAdd` can fix this issue by calling `String.valueOf` explicitly. It rewrites the example code into:

```scala
val str = String.valueOf(new AnyRef) + "foo"
```
