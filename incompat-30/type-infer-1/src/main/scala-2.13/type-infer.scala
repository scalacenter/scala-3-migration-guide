trait Foo { 
  type Inner
}

object Foo {
  val foo: Foo { type Inner = String } = ???
  
  def inner(foo: Foo): foo.Inner = ???

  def bar(f: String => Int): Option[Int] = Some(inner(foo)).map(f)
}
