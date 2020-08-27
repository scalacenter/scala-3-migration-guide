trait Count[+A] {
  def count(f: A => Boolean = (_: Any) => true): Int
}

trait Show[-A] {
  def show(value: List[A] = List.empty[Nothing]): String
}
