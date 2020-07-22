case class Foo(a: String, b: Int)

object Foo {
  def tuple(foo: Foo): (String, Int) = {
    Foo.unapply(foo).get
  }
}
