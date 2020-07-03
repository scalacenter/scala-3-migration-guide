object DoWhile {
  def foo(f: Int => Int): Int = {
    var i = 0
    do {
      i += 1
    } while (f(i) == 0)
    i
  }
}
