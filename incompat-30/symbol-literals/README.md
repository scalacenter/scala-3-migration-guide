## Symbol literals

The Symbol literal syntax is no longer supported, but the `scala.Symbol` class still exists so that each string literal can be safely replaced by an application of `Symbol`.

This piece of code does not compile in Scala 3:

```scala
val values: Map[Symbol, Int] = Map('abc -> 1)

val abc = values('abc)
```

#### Scala 2.13 deprecation

It produces a deprecation warning message when compiled in Scala 2.13 and the `-deprecation` option.

```
/src/main/scala/symbol-literals.scala:2:38: symbol literal is deprecated; use Symbol("abc") instead
  val values: Map[Symbol, Int] = Map('abc -> 1)
```

#### Scala 3 migration rewrite

Compiling with Scala 3 and the `-source:3.0-migration -rewrite` options can rewrite the code into:

```scala
val values: Map[Symbol, Int] = Map(Symbol("abc") -> 1)

val abc = values(Symbol("abc"))
```

Although `scala.Symbol` is useful for migration, beware that it is deprecated and that it will be removed from the `scala-library`.
You are recommended, as a second step, to replace them with plain string literals `"xwy"` or a dedicated class.