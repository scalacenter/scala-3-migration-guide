package foo

case class Foo(x: Int, b: Boolean)

object Bar {
  val f1 = (Foo.apply _).curried(1)(true)
  val f2 = (Foo.apply _).tupled((1, true))

  val fooCtr: (Int, Boolean) => Foo = (x, b) => Foo(x, b)

  val f1Bis = fooCtr.curried(1)(true)
  val f2Bis = fooCtr.tupled((2, false))
}
