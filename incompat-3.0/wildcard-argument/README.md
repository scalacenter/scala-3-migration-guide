## Wildcard type argument

Dotty cannot reduce the application of a higher-kinded abstract type member to the wildcard argument.

For instance:

```scala
trait Example {
  type Foo[A]

  def f(foos: Seq[Foo[_]]): Unit
}
```

Produces the following error when compiled with Dotty:

```
-- [E043] Type Error: /home/piquerez/scalacenter/scala-3-migration-guide/incompat-3.0/existential-type/wildcard-argument/target/src-managed/main/scala/wildcard-argument.scala:4:18 
4 |  def f(foos: Seq[Foo[_]]): Unit
  |                  ^^^^^^
  |unreducible application of higher-kinded type Example.this.Foo to wildcard arguments
```

We want method `f` to accept sequences of values of type `Foo[A]` with possibly different types of parameter `A`.
Dotty cannot reduce the application of `Foo` to the wildcard argument `_` because `Foo` is an abstract type member.
One possible solution is to introduce a wrapper class around `Foo`:

```scala
class FooWrapper[A](foo: Foo[A])

def f(foos: Seq[FooWrapper[_]]): Unit
```
