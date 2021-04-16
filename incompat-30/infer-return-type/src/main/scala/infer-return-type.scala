class Foo
class RichFoo(foo: Foo) extends Foo {
  def show: String = ""
}

class Parent {
  def foo: Foo = new Foo
}

class Child extends Parent {
  override def foo: RichFoo = new RichFoo(super.foo)
}

object Test {
  (new Child).foo.show
}
