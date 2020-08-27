trait Count[+A] {
  def count(f: A => Boolean = _ => true): Int
}

trait Show[-A] {
  def show(value: List[A] = List.empty): String
}
