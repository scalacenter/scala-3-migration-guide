trait IO[+A, +E] {
  def collectM[B, EE >: E](pf: PartialFunction[A, IO[B, EE]]): IO[B, EE]

  def collect[B](pf: PartialFunction[A, B]): IO[B, E] = collectM(pf.andThen(IO.succeed _))
}

object IO {
  def succeed[A](a: A): IO[A, Nothing] = ???
}
