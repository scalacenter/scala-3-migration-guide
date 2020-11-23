trait Example {
  type Foo[A]

  def f(foos: Seq[Foo[_]]): Unit
}
