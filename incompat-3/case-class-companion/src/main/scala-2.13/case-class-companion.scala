package foo

case class Foo(x: Int, b: Boolean)

object Bar {
  val f1 = Foo.curried(1)(true)
  val f2 = Foo.tupled((2, false))
}
