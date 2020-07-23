object Test {
  val f: Int => (=> Int) => Int = x => y => x
  val g: (Int, => Int) => Int = (x, y) => x
}
