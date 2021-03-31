class Foo[-A](x: List[A]) {
  def f[B](y: List[B]): Unit = ???
  def f(): Unit = f(x)
}

class Outer[+A](x: A) {
  class Inner[B >: A](y: B)
}
