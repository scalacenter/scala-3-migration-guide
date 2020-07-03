trait Container[A] {
  def weight: Int
}

object Container {
  def compare(x: Container[?], y: Container[?]): Int = {
    x.weight - y.weight
  }
}
