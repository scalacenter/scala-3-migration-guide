object DoWhile {
  def foo(f: Int => Int): Int = {
    var i = 0
    while ({ {
      i += 1
    } ; f(i) == 0}) ()
    i
  }
}
