trait Foo {
  def foo(): Unit 
}

object Foo {
  implicit def boolFoo(bool: Boolean): Foo = ???
  implicit def lazyBoolFoo(lazyBool:  => Boolean): Foo = ???

  boolFoo(true).foo()
}
