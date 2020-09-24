## Parentheses Around Lambda Parameter

When followed by its type, the parameter of a lambda is now required to be enclosed in parentheses, making the following Scala 2 code illegal:

```scala
val f = { x: Int => x * x }
```

#### Scala 3 migration rewrite

When compiled with Scala 3 and the `-source:3.0-migration -rewrite` option it is rewritten into:

```scala
val f = { (x: Int) => x * x }
```

#### Scalafix rule

The `fix.scala213.ParensAroundLambda` rule of [ohze/scala-rewrites](https://github.com/ohze/scala-rewrites/tree/dotty/#fixscala213parensaroundlambda) can also fix the incompatibility.
