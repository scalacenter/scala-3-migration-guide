// Here Dotty adds useless blank character
// trait Foo {
//   def print()
// }

class Bar(a: String) {
  def this() = {
    this("bar")
    print()
  }

  def print(): Unit = {
    println(a)
  }
}
