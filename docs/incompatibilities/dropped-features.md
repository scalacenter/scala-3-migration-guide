---
id: dropped-features
title: Dropped Features
---

Some features are dropped to simplify the language.

Most of these changes can be handled automatically during the [Scala 3 migration compilation](../tooling/scala-3-migration-mode.md).

||Scala 2.13|Scala 3 Migration Rewrite|Scalafix Rule|Comments|
|--- |--- |--- |--- |--- |
|[Symbol literals](dropped-features.md#symbol-literals)|Deprecation|✅|||
|[`do`-`while` construct](dropped-features.md#do-while-construct)||✅|||
|[Auto-application](dropped-features.md#auto-application)|Deprecation|✅|[✅](https://github.com/scala/scala-rewrites/blob/main/rewrites/src/main/scala/fix/scala213/ExplicitNonNullaryApply.scala)||
|[Value eta-expansion](dropped-features.md#value-eta-expansion)|Deprecation|✅|[✅](https://github.com/scala/scala-rewrites/blob/main/rewrites/src/main/scala/fix/scala213/ExplicitNullaryEtaExpansion.scala)||
|[`any2stringadd` conversion](dropped-features.md#any2stringadd-conversion)|Deprecation||[✅](https://github.com/scala/scala-rewrites/blob/main/rewrites/src/main/scala/fix/scala213/Any2StringAdd.scala)||
|[Early initializer](dropped-features.md#early-initializer)|Deprecation||||
|[Existential type](dropped-features.md#existential-type)|Feature warning||||

## Symbol literals

The Symbol literal syntax is deprecated in Scala 2.13 and dropped in Scala 3.
But the `scala.Symbol` class still exists so that each string literal can be safely replaced by an application of `Symbol`.

This piece of code does not compile in Scala 3:

```scala
val values: Map[Symbol, Int] = Map('abc -> 1)

val abc = values('abc)
```

The [Scala 3 migration compilation](../tooling/scala-3-migration-mode.md) rewrites the code into:

```scala
val values: Map[Symbol, Int] = Map(Symbol("abc") -> 1)

val abc = values(Symbol("abc"))
```

Although the `Symbol` class is useful during the transition, beware that it is deprecated and will be removed from the `scala-library` in a future version.
You are recommended, as a second step, to replace every use of `Symbol` with a plain string literals `"xwy"` or a custom dedicated class.

## `do`-`while` construct

The `do` keyword has acquired a different meaning in the [New Control Syntax](https://dotty.epfl.ch/docs/reference/other-new-features/control-syntax).

To avoid confusion, the traditional `do <body> while (<cond>)` construct is dropped.
It is recommended to use the equivalent `while ({ <body>; <cond> }) ()` that cross-compiles, or the new Scala 3 syntax `while { <body>; <cond> } do ()`.

The following piece of code does not compile in Scala 3.

```scala
do {
  i += 1
} while (f(i) == 0)
```

The [Scala 3 migration compilation](../tooling/scala-3-migration-mode.md) rewrites it into. 

```scala
while ({ {
  i += 1
} ; f(i) == 0}) ()
```

## Auto-application

Auto-application is the syntax of calling an empty-paren method such as `def toInt(): Int` without passing an empty argument list.
It is deprecated in Scala 2.13 and dropped in Scala 3.

The following code is now invalid:

```scala
object Hello {
  def message(): String = "Hello"
}

println(Hello.message)
```

In Scala 2.13 it produces a deprecation warning of the form:

```
src/main/scala/auto-application.scala:6:17: Auto-application to `()` is deprecated. Supply the empty argument list `()` explicitly to invoke method message,
[warn] or remove the empty argument list from its definition (Java-defined methods are exempt).
[warn] In Scala 3, an unapplied method like this will be eta-expanded into a function.
[warn]   println(Hello.message)
[warn]                 ^
```

The [Scala 3 migration compilation](../tooling/scala-3-migration-mode.md) rewrites it into:

```scala
object Hello {
  def message(): String = "Hello"
}

println(Hello.message())
```

Auto-application is covered in detail in [this page](https://dotty.epfl.ch/docs/reference/dropped-features/auto-apply.html) of the Scala 3 reference documentation.


## Value eta-expansion

Scala 3 introduces [automatic eta-expansion](https://dotty.epfl.ch/docs/reference/changed-features/eta-expansion-spec.html) which will deprecate the method to value syntax `m _`.
Furthermore Scala 3 does not allow eta-expansion of values to nullary functions anymore.

Thus this piece of code is now invalid:

```scala
val x = 1
val f: () => Int = x _
```

The [Scala 3 migration compilation](../tooling/scala-3-migration-mode.md) rewrites it into:

```scala
val x = 1
val f: () => Int = (() => x)
```

## `any2stringadd` conversion

The implicit `Predef.any2stringadd` conversion is deprecated in Scala 2.13 and dropped in Scala 3.

This piece of code does not compile anymore.

```scala
val str = new AnyRef + "foo"
```

The conversion to `String` must be applied explicitly, for instance with `String.valueOf`.

```scala
val str = String.valueOf(new AnyRef) + "foo"
```

This rewrite can be applied by the `fix.scala213.Any2StringAdd` Scalafix rule in [`scala/scala-rewrites`](https://index.scala-lang.org/scala/scala-rewrites/scala-rewrites/0.1.2?target=_2.13).

## Early Initializer

Early initializers are deprecated in Scala 2.13 and dropped in Scala 3.
They were rarely used, and mostly to compensate for the lack of [Trait parameters](https://dotty.epfl.ch/docs/reference/other-new-features/trait-parameters.html) which are now supported in Scala 3.

That is why the following piece of code does not compile anymore.

```scala
trait Bar {
  val name: String
  val size: Int = name.size
}

object Foo extends {
  val name = "Foo"
} with Bar
```

The Scala 3 compiler produces two error messages:

```
-- Error: src/main/scala/early-initializer.scala:6:19 
6 |object Foo extends {
  |                   ^
  |                   `extends` must be followed by at least one parent
```

```
-- [E009] Syntax Error: src/main/scala/early-initializer.scala:8:2 
8 |} with Bar
  |  ^^^^
  |  Early definitions are not supported; use trait parameters instead
```

It suggests to use trait parameters which gives us:

```scala
trait Bar(name: String) {
  val size: Int = name.size
}

object Foo extends Bar("Foo")
```

Since trait parameters are not available in Scala 2.13, it does not cross-compile.
If you need a cross-compiling solution you can use an intermediate class that carries the early initialized `val`s and `var`s as constructor parameters.

```scala
abstract class BarEarlyInit(val name: String) extends Bar

object Foo extends BarEarlyInit("Foo")
```

In the case of a class, it is also possible to use a secondary constructor with a fixed value, as shown by:

```scala
class Fizz private (val name: String) extends Bar {
  def this() = this("Fizz")
}
```

## Existential Type

Existential type is a [dropped feature](https://dotty.epfl.ch/docs/reference/dropped-features/existential-types.html), which makes the following code invalid.

```scala
def foo: List[Class[T]] forSome { type T }
```

> Existential type is an experimental feature in Scala 2.13 that must be enabled explicitly eather by importing `import scala.language.existentials` or by setting the `-language:existentials` compiler flag.

The proposed solution is to introduce an enclosing type that carries the dependent type:

```scala
trait Bar {
  type T
  val value: List[Class[T]]
}

def foo: Bar
```

Note that using a wildcard argument, `_` or `?`, is often simpler but is not always possible.

For instance you can replace `List[T] forSome { type  T }` by `List[?]`.
