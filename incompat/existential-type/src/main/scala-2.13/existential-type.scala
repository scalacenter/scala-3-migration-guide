trait Foo {
  def foo: List[Class[T]] forSome { type T }
}
