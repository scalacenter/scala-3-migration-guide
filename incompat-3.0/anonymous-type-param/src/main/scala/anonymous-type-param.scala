trait Foo[T]

object Foo {
  def foo[T: Foo]: Unit = ()
}
