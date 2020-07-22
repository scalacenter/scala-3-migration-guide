trait Bar {
  type T
  val value: List[Class[T]]
}

trait Foo {
  def foo: Bar
}
