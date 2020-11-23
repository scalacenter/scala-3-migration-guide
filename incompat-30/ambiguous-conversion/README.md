## Ambiguous Conversion On `A` And `=> A`

This incompatibility is inspired by this [commit](https://github.com/dotty-staging/scalacheck/commit/dc37c607cd9715c4e0ddc676314c6784cbf2beb5) in the [dotty-staging-fork](https://github.com/dotty-staging/scalacheck) of [scalacheck](https://github.com/typelevel/scalacheck).

In Scala 2 the implicit conversion on `Boolean` wins over the implicit conversion on `=> Boolean`.
It is not the case in Scala 3, causing incompatibilities in the form of ambiguous conversions.

```scala
trait Foo {
  def foo(): Unit 
}

implicit def boolFoo(bool: Boolean): Foo = ???
implicit def lazyBoolFoo(lazyBool:  => Boolean): Foo = ???

true.foo()
```

In this example the Scala 2 compiler chooses the `boolFoo` conversion whereas the Scala 3 compiler refuses to compile.

```
-- Error: src/main/scala/ambiguous-conversion.scala:4:19
9 |  true.foo()
  |  ^^^^
  |Found:    (true : Boolean)
  |Required: ?{ foo: ? }
  |Note that implicit extension methods cannot be applied because they are ambiguous;
  |both method boolFoo in object Foo and method lazyBoolFoo in object Foo provide an extension method `foo` on (true : Boolean)
```

A temporary solution is to write the conversion explicitly.

```scala
boolFoo(true).foo()
```
