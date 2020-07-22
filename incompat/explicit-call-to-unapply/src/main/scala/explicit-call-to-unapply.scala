case class Foo(a: String, b: Int)

object Foo {
  def tuple(foo: Foo): (String, Int) = {
    locally {
      val Foo(a, b) = foo
      (a, b)
    }
  }
}
