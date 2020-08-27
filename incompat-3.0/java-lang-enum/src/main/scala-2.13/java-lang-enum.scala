trait Foo {
  def foo[E <: Enum[E]](e: Enum[E]): Unit
}
