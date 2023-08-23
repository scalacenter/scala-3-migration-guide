trait Foo {
  def foo[A](a: A)(implicit ev: A => Long): Long = ev(a)
}
