trait Bar {
  val name: String
  val size: Int = name.size
}

object Foo extends {
  val name = "Foo"
} with Bar
