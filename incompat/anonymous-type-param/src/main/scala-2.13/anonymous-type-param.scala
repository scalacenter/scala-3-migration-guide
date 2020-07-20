trait Foo[T]

object Foo {
  def foo[_: Foo]: Unit = ()
}
