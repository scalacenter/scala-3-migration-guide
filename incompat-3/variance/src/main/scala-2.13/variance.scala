class Foo[-A](x: List[A]) {
  def f[B](y: List[B] = x): Unit = ???
}

class Outer[+A](x: A) {
  class Inner(y: A)
}
