object Foo {
  def foo(values: Array[String]): Unit = foo(values.toSeq:_*)

  def foo(values: String*): Unit = ???
}
