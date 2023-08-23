trait Example {
  type Foo[A]

  class FooWrapper[A](foo: Foo[A])

  def f(foos: Seq[FooWrapper[_]]): Unit
}
