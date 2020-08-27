trait Show[A]

object Test {
  def foo[F[_], A](f: F[A]): F[A] = ???

  def bar[A: Show, B: Show]: Show[A] = foo[Show, A](implicitly) 
}
