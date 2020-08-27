trait Foo[A]
trait Bar[A]

object Test {
  def toBar[A](foo: Foo[A]): Bar[A] = ???

  def fizz[A, B](foo: Foo[A]): Option[Foo[B]] = ???

  def buzz[A, B](foo: Foo[A]): Option[Bar[B]] = fizz[A, B](foo).map(toBar)
}
