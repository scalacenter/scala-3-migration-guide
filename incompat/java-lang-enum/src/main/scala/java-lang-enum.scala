trait Foo {
  def foo[E <: java.lang.Enum[E]](e: java.lang.Enum[E]): Unit
}
