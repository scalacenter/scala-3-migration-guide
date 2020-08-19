object EtaExpansion {
  def foo(x: Int)(y: Int): Int = x + y
  val f = foo _

  def bar(): Int = 3
  val g = bar _
}
