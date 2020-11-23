trait Context[M[_]]

trait Foo[T]
trait Bar[T]
object Fizz extends Foo[Int] with Bar[Int] 

object Foo {
  implicit val ctx: Context[Foo] = ???
}

object Test {
  def from[M[_], T](m: M[T])(implicit ctx: Context[M]): Unit = ???

  from(Fizz)
}
