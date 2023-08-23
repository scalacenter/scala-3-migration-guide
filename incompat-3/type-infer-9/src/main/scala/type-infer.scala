trait IO[+E]

trait Acquire[+E] {
  def apply[E1 >: E](release: E1 => IO[Nothing]): Release[E]
}

trait Release[+E] {
  def apply[E1 >: E](io: IO[E1]): IO[E1]
}


object IO {
  def cleanup[E](e: E): IO[Nothing] = ???
  def acquire[E]: Acquire[E] = ??? 

  def foo(io: IO[Nothing]): IO[Nothing] = acquire[Nothing](cleanup)(io)
}
