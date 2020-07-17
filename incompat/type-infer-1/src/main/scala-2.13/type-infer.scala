trait Context { type Out }

object Context {
  type Aux[A] = Context { type Out = A }
  implicit val ctx: Aux[String] = ???
}

trait Foo[A]

object Foo {
  def foo(implicit ctx: Context): Foo[ctx.Out] = ???

  def bar(f: Foo[String] => Int): Option[Int] = Option(foo).map(f)
}
