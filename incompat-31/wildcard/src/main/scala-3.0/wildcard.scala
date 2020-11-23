trait Container[A] {
  def weight: Int
}

object Container {
  def compare(x: Container[_], y: Container[_]): Int = {
    x.weight - y.weight
  }
}
