## Procedure Syntax

Procedure syntax has been deprecated in Scala 2 for a while and it is dropped in Scala 3, making the following code illegal:

```scala
trait Foo {
  def print()
}

object Bar {
  def print() {
    println("bar")
  }
}
```

#### Scala 2.13 deprecation

When compiled in Scala 2 with the `-deprecation` option it produces the following warnings:

```shell
src/main/scala/procedure-syntax.scala:2:14: procedure syntax is deprecated: instead, add `: Unit` to explicitly declare `print`'s return type
  def print()
             ^
src/main/scala/procedure-syntax.scala:6:15: procedure syntax is deprecated: instead, add `: Unit =` to explicitly declare `print`'s return type
  def print() {
              ^
```

#### Dotty migration rewrite

When compiled with Dotty and the `-source:3.0-migration -rewrite` options, the code is rewritten into:

```scala
trait Foo {
  def print(): Unit 
}

object Bar {
  def print(): Unit = {
    println("bar")
  }
}
```

#### Scalafix rule

Alternatively you can use the Scalafix rule named [`ProcedureSyntax`](https://scalacenter.github.io/scalafix/docs/rules/ProcedureSyntax.html).

For the case of secondary constructors, which is not handled by the `ProcedureSyntax` rule, you can use `fix.scala213.ConstructorProcedureSyntax` of [ohze/scala-rewrites/](https://github.com/ohze/scala-rewrites/tree/dotty/#fixscala213constructorproceduresyntax).
It turns `def this(...) { this(...) }` into `def this(...) = this(...)`.
