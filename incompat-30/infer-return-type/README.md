## Inferred Return Type of an Override Method

In Scala 2.13 the return type of an override method can be inferred from the left hand side, whereas in Scala 3 it would be inherited from the base method.

```scala
class Parent {
  def foo: Foo = new Foo
}

class Child extends Parent {
  override def foo = new RichFoo(super.foo)
}
```

In this example, `Child#foo` returns a `RichFoo` in its Scala 2.13 signature but a `Foo` in its Scala 3 signature.
It can lead to compiler errors as demonstrated below.

```scala
class Foo

class RichFoo(foo: Foo) extends Foo {
  def show: String = ""
}

class Parent {
  def foo: Foo = new Foo
}

class Child extends Parent {
  override def foo = new RichFoo(super.foo)
}

(new Child).foo.show // Scala 3 error: value show is not a member of Foo
```

In some rare cases involving implicit conversions and runtime casting it could even cause an runtime failure.

The solution is to make the return type of the override method explicit:

```diff
class Child extends Parent {
-  override def foo = new RichFoo(super.foo)
+  override def foo: RichFoo = new RichFoo(super.foo)
}
```
