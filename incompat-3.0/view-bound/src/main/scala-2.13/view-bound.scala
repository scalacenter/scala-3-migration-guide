trait Foo {
  def foo[A <% Long](a: A): Long = a
}
